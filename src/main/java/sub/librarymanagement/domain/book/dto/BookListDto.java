package sub.librarymanagement.domain.book.dto;

import java.util.List;

public record BookListDto (
        Integer totalPageNumber,
        int nowPageNumber,
        boolean isLast,
        List<BookDto> books
){
    public static BookListDto of(Integer totalPageNumber, int nowPageNumber, boolean isLast, List<BookDto> books){
        return new BookListDto(totalPageNumber, nowPageNumber, isLast, books);
    }
}