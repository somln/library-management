package sub.librarymanagement.domain.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sub.api.TagsApi;
import sub.librarymanagement.domain.book.service.TagService;
import sub.model.*;

@RestController
@RequiredArgsConstructor
public class TagController implements TagsApi {

    private final TagService tagService;

    @Override
    public ResponseEntity<ResponseDtoTagListDto> getTagList() {
        TagListDto tagList = tagService.getTagList();

        ResponseDtoTagListDto response = new ResponseDtoTagListDto().code(200).data(tagList);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoTagIdDto> createTag(TagInfoDto tagInfoDto) {
        TagIdDto tagIdDto = tagService.registerTag(tagInfoDto);

        ResponseDtoTagIdDto response = new ResponseDtoTagIdDto().code(200).data(tagIdDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseDtoTagIdDto> deleteTag(Long tagId) {
        TagIdDto tagIdDto = tagService.deleteTag(tagId);

        ResponseDtoTagIdDto response = new ResponseDtoTagIdDto().code(200).data(tagIdDto);
        return ResponseEntity.ok(response);
    }
}

