package sub.librarymanagement.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.dto.*;
import sub.librarymanagement.domain.loan.service.LoanRepository;
import sub.librarymanagement.persistence.book.entity.Book;
import sub.librarymanagement.persistence.book.entity.BookTag;
import sub.librarymanagement.persistence.book.entity.Tag;
import sub.librarymanagement.persistence.loan.entity.Loan;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public BookIdDto registerBook(BookInfoDto bookDto) {
        //bookDto에 포함된 tagId들로부터 Tag 객체들을 조회
        List<Tag> tags = bookRepository.findAllTagsById(bookDto.tagIds());

        //Book 객체 생성 및 저장
        Book book = Book.of(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());
        bookRepository.saveBook(book);
        if(bookDto.tagIds() != null || !bookDto.tagIds().isEmpty()) {
            //BookTag 객체들 생성 및 저장
            List<BookTag> bookTags = tags.stream()
                    .map(tag -> BookTag.of(book.getId(), tag.getId()))
                    .collect(Collectors.toList());
            bookRepository.saveAllBookTags(bookTags);
        }

        return BookIdDto.from(book.getId());
    }

    @Transactional
    public BookIdDto updateBook(Long bookId, BookInfoDto bookDto) {
        Book book = bookRepository.findBookById(bookId);
        book.update(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());

        bookRepository.deleteTagsByBookId(bookId); // 기존 태그 삭제

        if(bookDto.tagIds() != null || !bookDto.tagIds().isEmpty()) {
            List<Tag> tags = bookRepository.findAllTagsById(bookDto.tagIds());
            List<BookTag> bookTags = tags.stream()
                    .map(tag -> BookTag.of(book.getId(), tag.getId()))
                    .collect(Collectors.toList());
            bookRepository.saveAllBookTags(bookTags); // 새로운 태그 추가
        }

        return BookIdDto.from(book.getId());
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findBookById(bookId);
        //대출 중인 책이면 삭제 불가
        validateBookDeletion(bookId);
        //책 삭제 시, 책과 연관된 태그 삭제
        bookRepository.deleteTagsByBookId(bookId);
        bookRepository.deleteBook(book);

        // 해당 책과 연관된 모든 대출 기록에서 bookId를 제거하여 책이 삭제되더라도 대출 기록을 유지함
        List<Loan> loans = loanRepository.findByBookId(book.getId());
        loans.forEach(Loan::removeBook);
        bookRepository.deleteBook(book);
    }

    public void validateBookDeletion(Long bookId) {
        if (loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent()) {
            throw new ApplicationException(ErrorCode.CANNOT_DELETE_BOOK);
        }
    }

    public BookListDto getBookList(SortDto sortDto, BookTagListDto tagListDto, Pageable pageable) {
        Pageable sortedPageable = createSortedPageable(sortDto, pageable);

        Page<Book> bookPage;
        if (tagListDto.tags() == null || tagListDto.tags().isEmpty()) {
            //태그 필터링이 없는 경우
            bookPage = bookRepository.findAll(sortedPageable);
        } else {
            //태그 필터링이 있는 경우
            bookPage = bookRepository.findAllWithTagFiltering(tagListDto.tags(), sortedPageable);
        }

        return createBookListDto(bookPage);
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
        Book book = bookRepository.findBookById(bookId);
        return BookDto.of(book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublishDate(),
                convertTagsToDto(bookRepository.findAllTagsByBookId(book.getId())));
    }

    public BookListDto searchBook(SearchDto searchDto, Pageable pageable) {
        Page<Book> bookPage = bookRepository.search(searchDto.q(), pageable);
        return createBookListDto(bookPage);
    }

}
