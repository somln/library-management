package sub.librarymanagement.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sub.model.TagDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagCacheDto {
    private Long tagId;
    private String name;

    public static TagCacheDto from(TagDto tagDto) {
        return TagCacheDto.builder()
                .tagId(tagDto.getTagId())
                .name(tagDto.getName())
                .build();
    }

    public TagDto toTagDto() {
        return new TagDto()
                .tagId(this.tagId)
                .name(this.name);
    }
}