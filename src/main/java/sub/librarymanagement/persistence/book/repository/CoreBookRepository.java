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
import sub.librarymanagement.persistence.book.entity.BookTag;
import sub.librarymanagement.persistence.book.entity.Tag;
import sub.librarymanagement.persistence.loan.repository.JpaBookTagRepository;
import sub.librarymanagement.persistence.loan.repository.JpaTagRepository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class CoreBookRepository implements BookRepository {

    private final JpaBookRepository jpaBookRepository;
    private final JpaBookTagRepository jpaBookTagRepository;
    private final JpaTagRepository jpaTagRepository;

    @Override
    public void saveBook(Book book) {
        jpaBookRepository.save(book);
    }

    @Override
    public Book findBookById(Long bookId) {
        return jpaBookRepository.findById(bookId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BOOK_NOT_FOUND));
    }

    @Override
    public void deleteBook(Book book) {
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

    @Override
    public Page<Book> findAllWithTagFiltering(List<Long> tagIds, Pageable sortedPageable) {
        return jpaBookRepository.findAllWithTagFiltering(tagIds, sortedPageable);
    }

    @Override
    public List<Tag> findAllTagsById(List<Long> longs) {
        List<Tag> tags = jpaTagRepository.findAllById(longs);
        if (tags.size() != longs.size()) {
            throw new ApplicationException(ErrorCode.TAG_NOT_FOUND);
        }
        return tags;
    }

    @Override
    public void saveAllBookTags(List<BookTag> bookTags) {
        jpaBookTagRepository.saveAll(bookTags);
    }

    @Override
    public void deleteTagsByBookId(Long bookId) {
        jpaBookTagRepository.deleteByBookId(bookId);
    }

    @Override
    public List<Tag> findAllTagsByBookId(Long id) {
        return jpaBookTagRepository.findAllTagsByBookId(id);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return jpaTagRepository.findByName(name);
    }

    @Override
    public void saveTag(Tag tag) {
        jpaTagRepository.save(tag);
    }

    @Override
    public void findTagById(Long tagId) {
        jpaTagRepository.findById(tagId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TAG_NOT_FOUND));
    }

    @Override
    public boolean existsBookTagByTagId(Long tagId) {
        return jpaBookTagRepository.existsByTagId(tagId);
    }

    @Override
    public void deleteTagById(Long tagId) {
        jpaTagRepository.deleteById(tagId);
    }

    @Override
    public List<Tag> findAllTags() {
        return jpaTagRepository.findAll();
    }

}
