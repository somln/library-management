package sub.librarymanagement.persistence.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sub.librarymanagement.persistence.book.entity.Book;

import java.time.LocalDate;

public interface JpaBookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthorAndPublishDate(String title, String author, LocalDate publishDate);

    Page<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.author LIKE %:keyword%")
    Page<Book> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
