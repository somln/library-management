package sub.librarymanagement.domain.book.dto;

public record TagIdDto(
        Long tagId
) {
    public static TagIdDto from(Long tagId) {
        return new TagIdDto(tagId);
    }
}
