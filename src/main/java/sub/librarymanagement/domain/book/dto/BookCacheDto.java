package sub.librarymanagement.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sub.model.BookDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCacheDto {
    private Long id;
    private String title;
    private String author;
    private String publisher;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate publishDate;

    private List<TagCacheDto> tags;

    // BookDto -> BookCacheDto 변환 메서드
    public static BookCacheDto from(BookDto bookDto) {
        return BookCacheDto.builder()
                .id(bookDto.getId())
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .publisher(bookDto.getPublisher())
                .publishDate(bookDto.getPublishDate())
                .tags(bookDto.getTags().stream()
                        .map(TagCacheDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    // BookCacheDto -> BookDto 변환 메서드
    public BookDto toBookDto() {
        return new BookDto()
                .id(this.id)
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .publishDate(this.publishDate)
                .tags(this.tags.stream()
                        .map(TagCacheDto::toTagDto)
                        .collect(Collectors.toList()));
    }
}