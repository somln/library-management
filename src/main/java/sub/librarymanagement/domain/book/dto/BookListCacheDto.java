package sub.librarymanagement.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sub.model.BookListDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookListCacheDto {
    private int totalPageNumber;
    private int nowPageNumber;
    @JsonProperty("last")
    private boolean isLast;
    private List<BookCacheDto> books;

    public static BookListCacheDto from(BookListDto bookListDto) {
        return BookListCacheDto.builder()
                .totalPageNumber(bookListDto.getTotalPageNumber())
                .nowPageNumber(bookListDto.getNowPageNumber())
                .isLast(bookListDto.getIsLast())
                .books(bookListDto.getBooks().stream()
                        .map(BookCacheDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public BookListDto toBookListDto() {
        return new BookListDto()
                .totalPageNumber(this.totalPageNumber)
                .nowPageNumber(this.nowPageNumber)
                .isLast(this.isLast)
                .books(this.books.stream()
                        .map(BookCacheDto::toBookDto)
                        .collect(Collectors.toList()));
    }
}
