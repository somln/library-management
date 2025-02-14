package sub.librarymanagement.persistence.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "book_tags")
public class BookTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private Long tagId;

    public static BookTag of(Long bookId, Long tagId) {
        return BookTag.builder()
                .bookId(bookId)
                .tagId(tagId)
                .build();
    }

}