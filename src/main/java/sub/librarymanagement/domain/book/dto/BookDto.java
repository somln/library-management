package sub.librarymanagement.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.List;

public record BookDto(
        Long id,
        String title,
        String author,
        String publisher,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate publishDate,
        List<TagDto> tags
) {
    public static BookDto of(Long id, String title, String author, String publisher, LocalDate publishDate, List<TagDto> tags) {
        return new BookDto(id, title, author, publisher, publishDate, tags);
    }
}
