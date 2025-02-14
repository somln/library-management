package sub.librarymanagement.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.cache.CacheService;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.dto.*;
import sub.librarymanagement.domain.loan.service.LoanRepository;
import sub.librarymanagement.persistence.book.entity.Book;
import sub.librarymanagement.persistence.book.entity.BookTag;
import sub.librarymanagement.persistence.book.entity.Tag;
import sub.librarymanagement.persistence.loan.entity.Loan;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final CacheService cacheService;

    @Transactional
    public BookIdDto registerBook(BookInfoDto bookDto) {
        List<Tag> tags = bookRepository.findAllTagsById(bookDto.tagIds());

        Book book = Book.of(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());
        bookRepository.saveBook(book);

        if (bookDto.tagIds() != null && !bookDto.tagIds().isEmpty()) {
            List<BookTag> bookTags = tags.stream()
                    .map(tag -> BookTag.of(book.getId(), tag.getId()))
                    .collect(Collectors.toList());
            bookRepository.saveAllBookTags(bookTags);
        }

        cacheService.evictAll("books", "book-search"); // 전체 캐시 무효화
        return BookIdDto.from(book.getId());
    }

    @Transactional
    public BookIdDto updateBook(Long bookId, BookInfoDto bookDto) {
        Book book = bookRepository.findBookById(bookId);
        book.update(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());

        bookRepository.deleteTagsByBookId(bookId); // 기존 태그 삭제

        if (bookDto.tagIds() != null && !bookDto.tagIds().isEmpty()) {
            List<Tag> tags = bookRepository.findAllTagsById(bookDto.tagIds());
            List<BookTag> bookTags = tags.stream()
                    .map(tag -> BookTag.of(book.getId(), tag.getId()))
                    .collect(Collectors.toList());
            bookRepository.saveAllBookTags(bookTags);
        }

        cacheService.evictAll("books", "book-search");
        cacheService.evictCache("book", bookId.toString());

        return BookIdDto.from(book.getId());
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findBookById(bookId);
        validateBookDeletion(bookId);

        bookRepository.deleteTagsByBookId(bookId);
        bookRepository.deleteBook(book);

        List<Loan> loans = loanRepository.findByBookId(book.getId());
        loans.forEach(Loan::removeBook);
        bookRepository.deleteBook(book);

        cacheService.evictAll("books", "book-search");
        cacheService.evictCache("book", bookId.toString());
    }

    public void validateBookDeletion(Long bookId) {
        if (loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent()) {
            throw new ApplicationException(ErrorCode.CANNOT_DELETE_BOOK);
        }
    }

    public BookListDto getBookList(SortDto sortDto, BookTagListDto tagListDto, Pageable pageable) {
        String cacheKey = generateBookCacheKey(sortDto, pageable, tagListDto);

        // 캐시 확인
        BookListDto cachedResult = cacheService.getCache("books", cacheKey, BookListDto.class);
        if (cachedResult != null) {
            return cachedResult;
        }

        Pageable sortedPageable = createSortedPageable(sortDto, pageable);
        Page<Book> bookPage = (tagListDto.tags() == null || tagListDto.tags().isEmpty())
                ? bookRepository.findAll(sortedPageable)
                : bookRepository.findAllWithTagFiltering(tagListDto.tags(), sortedPageable);

        BookListDto result = createBookListDto(bookPage);

        cacheService.setCache("books", cacheKey, result, Duration.ofMinutes(30)); // TTL 30분

        return result;
    }


    private String generateBookCacheKey(SortDto sortDto, Pageable pageable, BookTagListDto tagListDto) {
        String tagPart = getTagCacheKey(tagListDto);
        return String.format("books:%s:%s:%d:%d:%s",
                sortDto.sortBy(), sortDto.sortDirection(), pageable.getPageNumber(), pageable.getPageSize(), tagPart);
    }

    private String getTagCacheKey(BookTagListDto tagListDto) {
        return (tagListDto.tags() == null || tagListDto.tags().isEmpty())
                ? "no-tags"
                : "tags-" + tagListDto.tags().stream().sorted().map(String::valueOf).collect(Collectors.joining(","));
    }

    private Pageable createSortedPageable(SortDto sortDto, Pageable pageable) {
        Sort.Direction direction = sortDto.sortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortDto.sortBy());
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private BookListDto createBookListDto(Page<Book> bookPage) {
        List<BookDto> bookList = bookPage.getContent().stream()
                .map(book -> BookDto.of(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getPublishDate(),
                        convertTagsToDto(bookRepository.findAllTagsByBookId(book.getId()))
                ))
                .toList();

        return BookListDto.of(bookPage.getTotalPages(), bookPage.getNumber(), bookPage.isLast(), bookList);
    }

    private List<TagDto> convertTagsToDto(List<Tag> tags) {
        return tags.stream()
                .map(tag -> TagDto.of(tag.getId(), tag.getName()))
                .toList();
    }

    public BookDto getBook(Long bookId) {
        // 캐시 확인
        BookDto cachedBook = cacheService.getCache("book", bookId.toString(), BookDto.class);
        if (cachedBook != null) {
            return cachedBook;
        }

        Book book = bookRepository.findBookById(bookId);
        BookDto bookDto = BookDto.of(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublishDate(),
                convertTagsToDto(bookRepository.findAllTagsByBookId(book.getId()))
        );

        cacheService.setCache("book", bookId.toString(), bookDto, Duration.ofMinutes(60)); // TTL 60분

        return bookDto;
    }

    public BookListDto searchBook(SearchDto searchDto, Pageable pageable) {
        String cacheKey = String.format("book-search:%s:%d:%d", searchDto.q(), pageable.getPageNumber(), pageable.getPageSize());

        // 캐시 확인
        BookListDto cachedResult = cacheService.getCache("book-search", cacheKey, BookListDto.class);
        if (cachedResult != null) {
            return cachedResult;
        }

        Page<Book> bookPage = bookRepository.search(searchDto.q(), pageable);
        BookListDto result = createBookListDto(bookPage);

        cacheService.setCache("book-search", cacheKey, result, Duration.ofMinutes(5)); // TTL 5분

        return result;
    }
}
