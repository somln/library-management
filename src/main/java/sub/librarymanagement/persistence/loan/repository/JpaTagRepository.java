package sub.librarymanagement.persistence.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sub.librarymanagement.persistence.book.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface JpaTagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
