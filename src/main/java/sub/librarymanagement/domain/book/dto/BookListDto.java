package sub.librarymanagement.domain.book.dto;

import java.util.List;

public record BookListDto (
        int totalPageNumber,
        int nowPageNumber,
        boolean isLast,
        List<BookDto> books
){
    public static BookListDto of(int totalPageNumber, int nowPageNumber, boolean isLast, List<BookDto> books){
        return new BookListDto(totalPageNumber, nowPageNumber, isLast, books);
    }
}