package sub.librarymanagement.domain.book.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sub.librarymanagement.common.util.ResponseDto;
import sub.librarymanagement.domain.book.dto.*;
import sub.librarymanagement.domain.book.service.BookService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/admin/books")
    public ResponseEntity<ResponseDto<BookIdDto>> registerBook(
            @Valid @RequestBody RegisterBookDto registerBookDto) {
        return ResponseEntity.ok(ResponseDto.okWithData(bookService.registerBook(registerBookDto)));
    }

    @PutMapping("/admin/books/{bookId}")
    public ResponseEntity<ResponseDto<BookIdDto>> updateBook(
            @PathVariable Long bookId,
            @Valid @RequestBody RegisterBookDto registerBookDto) {
        return ResponseEntity.ok(ResponseDto.okWithData(bookService.updateBook(bookId, registerBookDto)));
    }

    @DeleteMapping("/admin/books/{bookId}")
    public ResponseEntity<ResponseDto<Void>> deleteBook(
            @PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/books")
    public ResponseEntity<ResponseDto<BookListDto>> getBookList(Pageable pageable) {
        return ResponseEntity.ok(ResponseDto.okWithData(bookService.getBookList(pageable)));
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<ResponseDto<BookDto>> getBook(
            @PathVariable Long bookId) {
        return ResponseEntity.ok(ResponseDto.okWithData(bookService.getBook(bookId)));
    }

    @GetMapping("/books/search")
    public ResponseEntity<ResponseDto<BookListDto>> searchBook(
            @ModelAttribute @Valid SearchDto searchDto,
            Pageable pageable) {
        return ResponseEntity.ok(ResponseDto.okWithData(bookService.searchBook(searchDto, pageable)));
    }
}


