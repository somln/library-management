package sub.librarymanagement.persistence.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sub.librarymanagement.persistence.book.entity.Book;

public interface JpaBookRepository extends JpaRepository<Book, String> {
}
