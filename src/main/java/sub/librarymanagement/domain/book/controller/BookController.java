package sub.librarymanagement.domain.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sub.librarymanagement.domain.book.service.BookService;

@RestController
@RequestMapping("api//books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
}
