package sub.librarymanagement.domain.book.dto;

import java.util.List;

public record TagListDto(
        List<TagDto> tags
) {
    public static TagListDto from(List<TagDto> tagList) {
        return new TagListDto(tagList);
    }
}
