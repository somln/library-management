package sub.librarymanagement.domain.book.dto;

import java.time.LocalDate;
import java.util.List;

public record BookDto(
        Long id,
        String title,
        String author,
        String publisher,
        LocalDate publishDate,
        List<TagDto> tags
) {
    public static BookDto of(Long id, String title, String author, String publisher, LocalDate publishDate, List<TagDto> tags) {
        return new BookDto(id, title, author, publisher, publishDate, tags);
    }
}
