package sub.librarymanagement.persistence.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sub.librarymanagement.persistence.book.entity.BookTag;
import sub.librarymanagement.persistence.book.entity.Tag;

import java.util.List;


public interface JpaBookTagRepository extends JpaRepository<BookTag, Long> {
    void deleteByBookId(Long bookId);

    @Query("SELECT t FROM BookTag bt " +
            "JOIN Tag t ON bt.tagId = t.id " +
            "WHERE bt.bookId = :id")
    List<Tag> findAllTagsByBookId(Long id);

    boolean existsByTagId(Long tagId);
}
