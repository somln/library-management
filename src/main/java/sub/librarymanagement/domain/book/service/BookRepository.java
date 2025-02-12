package sub.librarymanagement.domain.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sub.librarymanagement.domain.book.dto.SearchDto;
import sub.librarymanagement.persistence.book.entity.Book;
import java.time.LocalDate;

public interface BookRepository {
    boolean existsByTitleAndAuthorAndPublishDate(String title, String author, LocalDate publishDate);

    void save(Book book);

    Book findById(Long bookId);

    void delete(Book book);

    Page<Book> findAllPagination(Pageable pageable);

    Page<Book> search(String keyword, Pageable pageable);
}
