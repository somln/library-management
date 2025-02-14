package sub.librarymanagement.domain.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sub.librarymanagement.persistence.book.entity.Book;
import sub.librarymanagement.persistence.book.entity.BookTag;
import sub.librarymanagement.persistence.book.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void saveBook(Book book);

    Book findBookById(Long bookId);

    void deleteBook(Book book);

    Page<Book> findAll(Pageable pageable);

    Page<Book> search(String keyword, Pageable pageable);

    Page<Book> findAllWithTagFiltering(List<Long> strings, Pageable sortedPageable);

    List<Tag> findAllTagsById(List<Long> longs);

    void saveAllBookTags(List<BookTag> bookTags);

    void deleteTagsByBookId(Long bookId);

    List<Tag> findAllTagsByBookId(Long id);

    Optional<Tag> findTagByName(String name);

    void saveTag(Tag tag);

    void findTagById(Long tagId);

    boolean existsBookTagByTagId(Long tagId);

    void deleteTagById(Long tagId);

    List<Tag> findAllTags();

}
