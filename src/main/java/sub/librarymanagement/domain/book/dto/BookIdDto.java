package sub.librarymanagement.domain.book.dto;

public record BookIdDto(
        Long bookId
) {
    public static BookIdDto from(Long bookId) {
        return new BookIdDto(bookId);
    }
}
