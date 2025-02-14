package sub.librarymanagement.domain.book.dto;

public record TagDto(
        Long tagId,
        String name
) {
    public static TagDto of(Long tagId, String name) {
        return new TagDto(tagId, name);
    }
}
