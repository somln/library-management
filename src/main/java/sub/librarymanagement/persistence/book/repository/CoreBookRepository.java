package sub.librarymanagement.persistence.book.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.service.BookRepository;
import sub.librarymanagement.persistence.book.entity.Book;

import java.time.LocalDate;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreBookRepository implements BookRepository {

    private final JpaBookRepository jpaBookRepository;

    @Override
    public void save(Book book) {
        jpaBookRepository.save(book);
    }

    @Override
    public Book findById(Long bookId) {
        return jpaBookRepository.findById(bookId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOOK_NOT_FOUND));
    }

    @Override
    public void delete(Book book) {
        jpaBookRepository.delete(book);
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return jpaBookRepository.findAll(pageable);
    }

    @Override
    public Page<Book> search(String keyword, Pageable pageable) {
        return jpaBookRepository.searchByKeyword(keyword, pageable);
    }

}
