package sub.librarymanagement.persistence.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @NotNull
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
