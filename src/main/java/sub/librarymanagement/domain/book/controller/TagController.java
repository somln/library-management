package sub.librarymanagement.domain.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sub.librarymanagement.common.util.ResponseDto;
import sub.librarymanagement.domain.book.dto.TagIdDto;
import sub.librarymanagement.domain.book.dto.TagInfoDto;
import sub.librarymanagement.domain.book.dto.TagListDto;
import sub.librarymanagement.domain.book.service.TagService;

@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ResponseDto<TagIdDto>> registerTag(
            @RequestBody TagInfoDto tagInfoDto) {
        return ResponseEntity.ok(ResponseDto.okWithData(tagService.registerTag(tagInfoDto)));
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<ResponseDto<TagIdDto>> deleteTag(
            @PathVariable Long tagId) {
        return ResponseEntity.ok(ResponseDto.okWithData(tagService.deleteTag(tagId)));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<TagListDto>> getTagList() {
        return ResponseEntity.ok(ResponseDto.okWithData(tagService.getTagList()));
    }
 }
