package sub.librarymanagement.domain.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sub.librarymanagement.persistence.book.entity.Book;
import java.time.LocalDate;

public interface BookRepository {
    void save(Book book);

    Book findById(Long bookId);

    void delete(Book book);

    Page<Book> findAll(Pageable pageable);

    Page<Book> search(String keyword, Pageable pageable);
}
