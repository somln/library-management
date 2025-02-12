package sub.librarymanagement.domain.book.dto;

import java.time.LocalDate;

public record BookDto(
        Long id,
        String title,
        String author,
        String publisher,
        LocalDate publishDate
) {
    public static BookDto of(Long id, String title, String author, String publisher, LocalDate publishDate) {
        return new BookDto(id, title, author, publisher, publishDate);
    }
}
