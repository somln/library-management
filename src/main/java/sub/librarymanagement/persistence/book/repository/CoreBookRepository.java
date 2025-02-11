package sub.librarymanagement.persistence.book.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.domain.book.service.BookRepository;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreBookRepository implements BookRepository {

    private final JpaBookRepository jpaBookRepository;
}
