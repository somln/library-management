package sub.librarymanagement.persistence.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private LocalDate publishDate;

    public static Book of(String title, String author, String publisher, LocalDate publishDate) {
        return Book.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .publishDate(publishDate)
                .build();
    }

    public void update(String title, String author, String publisher, LocalDate publishDate) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
    }
}
