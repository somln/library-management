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
import sub.librarymanagement.persistence.loan.entity.Loan;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public BookIdDto registerBook(RegisterBookDto bookDto) {
        Book book = Book.of(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());
        bookRepository.save(book);
        return BookIdDto.from(book.getId());
    }

    @Transactional
    public BookIdDto updateBook(Long bookId, RegisterBookDto bookDto) {
        Book book = bookRepository.findById(bookId);
        book.update(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());
        return BookIdDto.from(book.getId());
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId);
        validateBookDeletion(bookId);
        deleteBook(book);
    }

    //대출 중인 책이면 삭제 불가
    public void validateBookDeletion(Long bookId) {
        if (loanRepository.findByBookIdAndReturnedFalse(bookId).isPresent()) {
            throw new ApplicationException(ErrorCode.CANNOT_DELETE_BOOK);
        }
    }

    @Transactional
    public void deleteBook(Book book) {
        // 해당 책과 연관된 모든 대출 기록에서 bookId를 제거하여 책이 삭제되더라도 대출 기록을 유지함
        List<Loan> loans = loanRepository.findByBookId(book.getId());
        loans.forEach(Loan::removeBook);
        bookRepository.delete(book);
    }

    public BookListDto getBookList(SortDto sortDto, Pageable pageable) {
        Pageable sortedPageable = createSortedPageable(sortDto, pageable);
        Page<Book> bookPage = bookRepository.findAll(sortedPageable);
        return createBookListDto(bookPage);
    }

    private Pageable createSortedPageable(SortDto sortDto, Pageable pageable) {
        Sort.Direction direction = sortDto.sortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortDto.sortBy());
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private BookListDto createBookListDto(Page<Book> bookPage) {
        List<BookDto> bookList = bookPage.getContent().stream()
                .map(book -> BookDto.of(book.getId(), book.getTitle(), book.getAuthor(), book.getPublisher(), book.getPublishDate()))
                .toList();
        return BookListDto.of(bookPage.getTotalPages(), bookPage.getNumber(), bookPage.isLast(), bookList);
    }

    public BookDto getBook(Long bookId) {
        Book book = bookRepository.findById(bookId);
        return BookDto.of(book.getId(), book.getTitle(), book.getAuthor(), book.getPublisher(), book.getPublishDate());
    }

    public BookListDto searchBook(SearchDto searchDto, Pageable pageable) {
        Page<Book> bookPage = bookRepository.search(searchDto.q(), pageable);
        return createBookListDto(bookPage);
    }

}
