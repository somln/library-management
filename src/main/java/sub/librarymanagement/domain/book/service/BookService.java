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
import sub.librarymanagement.persistence.book.entity.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookIdDto registerBook(RegisterBookDto bookDto) {
        validateBookInfo(bookDto);
        Book book = Book.of(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());
        bookRepository.save(book);
        return BookIdDto.from(book.getId());
    }

    private void validateBookInfo(RegisterBookDto bookDto) {
        //제목, 저자, 출판일이 모두 같은 책이 이미 존재하는지 확인
        if (bookRepository.existsByTitleAndAuthorAndPublishDate(bookDto.title(), bookDto.author(), bookDto.publishDate())) {
            throw new ApplicationException(ErrorCode.BOOK_DUPLICATION);
        }
    }

    @Transactional
    public BookIdDto updateBook(Long bookId, RegisterBookDto bookDto) {
        Book book = bookRepository.findById(bookId);
        book.update(bookDto.title(), bookDto.author(), bookDto.publisher(), bookDto.publishDate());
        return BookIdDto.from(book.getId());
    }


    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId);
        bookRepository.delete(book);
    }

    public BookListDto getBookList(SortDto sortDto, Pageable pageable) {
        Sort.Direction direction = sortDto.sortDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortDto.sortBy());

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<Book> bookPage = bookRepository.findAll(sortedPageable);
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
        List<BookDto> bookList = bookPage.getContent().stream().map(book -> BookDto.of(book.getId(), book.getTitle(),
                book.getAuthor(), book.getPublisher(), book.getPublishDate())).toList();
        return BookListDto.of(bookPage.getTotalPages(), bookPage.getNumber(), bookPage.isLast(), bookList);
    }
}
