package sub.librarymanagement.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.cache.CacheService;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.dto.BookCacheDto;
import sub.librarymanagement.domain.book.dto.BookListCacheDto;
import sub.librarymanagement.domain.loan.service.LoanRepository;
import sub.librarymanagement.persistence.book.entity.Book;
import sub.librarymanagement.persistence.book.entity.BookTag;
import sub.librarymanagement.persistence.book.entity.Tag;
import sub.librarymanagement.persistence.loan.entity.Loan;
import sub.model.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final CacheService cacheService; // Assuming CacheService is the same as in the first service

    @Transactional
    public BookIdDto registerBook(BookInfoDto bookDto) {
        // Book 객체 생성 및 저장
        Book book = Book.of(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPublisher(), bookDto.getPublishDate());
        bookRepository.saveBook(book);

        // BookTag 객체들 생성 및 저장
        saveBookTags(bookDto.getTagIds(), book);

        // Cache eviction
        cacheService.evictAll("books", "book-search");
        return new BookIdDto().bookId(book.getId()+ 1000);
    }

    @Transactional
    public BookIdDto updateBook(Long bookId, BookInfoDto bookDto) {

        Book book = bookRepository.findBookById(bookId);
        book.update(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getAuthor(), bookDto.getPublishDate());

        bookRepository.deleteTagsByBookId(bookId); // 기존 태그 삭제

        // BookTag 객체들 생성 및 저장
        saveBookTags(bookDto.getTagIds(), book);

        cacheService.evictCache("book", bookId.toString());
        cacheService.evictAll("books", "book-search");

        return new BookIdDto().bookId(bookId);
    }

    private void saveBookTags(List<Long> tagIds, Book book) {
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = bookRepository.findAllTagsById(tagIds);
            List<BookTag> bookTags = tags.stream()
                    .map(tag -> BookTag.of(book.getId(), tag.getId()))
                    .collect(Collectors.toList());
            bookRepository.saveAllBookTags(bookTags);
        }
    }

    @Transactional
    public BookIdDto deleteBook(Long bookId) {
        Book book = bookRepository.findBookById(bookId);
        // 대출 중인 책은 삭제할 수 없음
        validateBookDeletion(bookId);

        //책, 첵-태그 관계 삭제
        bookRepository.deleteTagsByBookId(bookId);
        bookRepository.deleteBook(book);

        // 해당 책과 연관된 모든 대출 기록에서 bookId를 제거하여 책이 삭제되더라도 대출 기록을 유지함
        List<Loan> loans = loanRepository.findByBookId(book.getId());
        loans.forEach(Loan::removeBook);
        bookRepository.deleteBook(book);

        cacheService.evictCache("book", bookId.toString());
        cacheService.evictAll("books", "book-search");

        return new BookIdDto().bookId(bookId);
    }

    private void validateBookDeletion(Long bookId) {
        if (loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent()) {
            throw new ApplicationException(ErrorCode.CANNOT_DELETE_BOOK);
        }
    }

    public BookListDto getBookList(String sort, List<Long> tags, Pageable pageable) {
        String cacheKey = String.format("books:%s:%s:%d:%d", sort, tags, pageable.getPageNumber(), pageable.getPageSize());

        // 캐시에서 먼저 조회
        BookListCacheDto cachedResult = cacheService.getCache("books", cacheKey, BookListCacheDto.class);
        if (cachedResult != null) {
            return cachedResult.toBookListDto();
        }

        Pageable sortedPageable = createSortedPageable(sort, pageable);

        Page<Book> bookPage;
        if (tags == null || tags.isEmpty()) {
            // 태그 필터링이 없는 경우
            bookPage = bookRepository.findAll(sortedPageable);
        } else {
            // 태그 필터링이 있는 경우
            bookPage = bookRepository.findAllWithTagFiltering(tags, sortedPageable);
        }

        BookListDto result = createBookListDto(bookPage);

        // 캐시에 저장 (TTL 30분 설정)
        cacheService.setCache("books", cacheKey, BookListCacheDto.from(result), Duration.ofMinutes(30));

        return result;
    }

    private Pageable createSortedPageable(String sort, Pageable pageable) {
        Sort.Direction direction = Sort.Direction.ASC;
        String sortBy = "title";

        if (sort != null && sort.equalsIgnoreCase("publishDate")) {
            sortBy = "publishDate";
        }
        Sort sorted = Sort.by(direction, sortBy);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorted);
    }

    private BookListDto createBookListDto(Page<Book> bookPage) {
        List<BookDto> bookList = bookPage.getContent().stream()
                .map(this::createBookDto)
                .toList();
        return new BookListDto()
                .totalPageNumber(bookPage.getTotalPages())
                .nowPageNumber(bookPage.getNumber())
                .isLast(bookPage.isLast())
                .books(bookList);
    }

    @NotNull
    private BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publishDate(book.getPublishDate());

        List<Tag> tags = bookRepository.findAllTagsByBookId(book.getId());
        bookDto.setTags(convertTagsToDto(tags));
        return bookDto;
    }

    private List<TagDto> convertTagsToDto(List<Tag> tags) {
        return tags.stream()
                .map(tag -> new TagDto()
                        .tagId(tag.getId())
                        .name(tag.getName()))
                .toList();
    }

    public BookDto getBook(Long bookId) {
        // 캐시에서 먼저 조회
        BookCacheDto cachedBook = cacheService.getCache("book", bookId.toString(), BookCacheDto.class);
        if (cachedBook != null) {
            return cachedBook.toBookDto();
        }

        // 캐시에 없으면 DB 조회 후 저장
        Book book = bookRepository.findBookById(bookId);
        BookDto bookDto = createBookDto(book);

        // 캐시에 저장 (60분 유지)
        cacheService.setCache("book", bookId.toString(), BookCacheDto.from(bookDto), Duration.ofMinutes(60));

        return bookDto;
    }

    public BookListDto searchBook(String q, Pageable pageable) {
        String cacheKey = String.format("book-search:%s:%d:%d", q, pageable.getPageNumber(), pageable.getPageSize());

        // 캐시에서 먼저 조회
        BookListCacheDto cachedResult = cacheService.getCache("book-search", cacheKey, BookListCacheDto.class);
        if (cachedResult != null) {
            return cachedResult.toBookListDto();
        }

        // 캐시에 없으면 DB에서 조회
        Page<Book> bookPage = bookRepository.search(q, pageable);
        BookListDto result = createBookListDto(bookPage);

        // 캐시에 저장 (TTL 5분 설정)
        cacheService.setCache("book-search", cacheKey, BookListCacheDto.from(result), Duration.ofMinutes(5));
        return result;
    }

}
