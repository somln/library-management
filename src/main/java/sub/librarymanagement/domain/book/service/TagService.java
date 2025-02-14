package sub.librarymanagement.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.domain.book.dto.TagDto;
import sub.librarymanagement.domain.book.dto.TagIdDto;
import sub.librarymanagement.domain.book.dto.TagInfoDto;
import sub.librarymanagement.domain.book.dto.TagListDto;
import sub.librarymanagement.persistence.book.entity.Tag;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final BookRepository bookRepository;

    public TagIdDto registerTag(TagInfoDto tagInfoDto) {
        validateTagDuplication(tagInfoDto.name());
        Tag tag = Tag.of(tagInfoDto.name());
        bookRepository.saveTag(tag);
        return TagIdDto.from(tag.getId());
    }

    private void validateTagDuplication(String name) {
        if (bookRepository.findTagByName(name).isPresent()) {
            throw new ApplicationException(ErrorCode.TAG_DUPLICATION);
        }
    }

    public TagIdDto deleteTag(Long tagId) {
        bookRepository.findTagById(tagId);
        validateTagDeletion(tagId);
        bookRepository.deleteTagById(tagId);
        return TagIdDto.from(tagId);
    }

    private void validateTagDeletion(Long tagId) {
        if (bookRepository.existsBookTagByTagId(tagId)) {
            throw new ApplicationException(ErrorCode.TAG_DELETION);
        }
    }

    public TagListDto getTagList() {
        List<TagDto> tagList = bookRepository.findAllTags()
                .stream()
                .map(tag -> TagDto.of(tag.getId(), tag.getName()))
                .toList();
        return TagListDto.from(tagList);
    }
}
