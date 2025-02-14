package sub.librarymanagement.persistence.book.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sub.librarymanagement.persistence.book.entity.Book;

import java.util.List;

public interface JpaBookRepository extends JpaRepository<Book, Long> {

    @NotNull
    Page<Book> findAll(@NotNull Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.author LIKE %:keyword%")
    Page<Book> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT b " +
            "FROM Book b " +
            "LEFT JOIN BookTag bt ON b.id = bt.bookId " +
            "WHERE bt.tagId IN :tagIds")
    Page<Book> findAllWithTagFiltering(List<Long> tagIds, Pageable sortedPageable);
}
