package sub.librarymanagement.domain.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sub.api.BooksApi;
import sub.model.*;
import sub.librarymanagement.domain.book.service.BookService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookController implements BooksApi {

    private final BookService bookService;

    @Override
    public ResponseEntity<ResponseDtoBookIdDto> createBook(BookInfoDto bookInfoDto) {
        BookIdDto bookIdDto = bookService.registerBook(bookInfoDto);

        ResponseDtoBookIdDto response = new ResponseDtoBookIdDto().code(200).data(bookIdDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoBookIdDto> deleteBook(Long bookId) {
        BookIdDto bookIdDto = bookService.deleteBook(bookId);

        ResponseDtoBookIdDto response = new ResponseDtoBookIdDto().code(200).data(bookIdDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoBookListDto> getBookList(String sort, List<Long> tags, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(20)
        );

        BookListDto result = bookService.getBookList(sort, tags, pageable);

        ResponseDtoBookListDto response = new ResponseDtoBookListDto().code(200).data(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoBookDto> getBook(Long bookId) {
        BookDto result = bookService.getBook(bookId);

        ResponseDtoBookDto response = new ResponseDtoBookDto().code(200).data(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoBookListDto> searchBooks(String q, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(20)
        );

        BookListDto result = bookService.searchBook(q, pageable);

        ResponseDtoBookListDto response = new ResponseDtoBookListDto().code(200).data(result);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoBookIdDto> updateBook(Long bookId, BookInfoDto bookInfoDto) {
        BookIdDto bookIdDto = bookService.updateBook(bookId, bookInfoDto);

        ResponseDtoBookIdDto response = new ResponseDtoBookIdDto().code(200).data(bookIdDto);
        return ResponseEntity.ok(response);
    }

}

