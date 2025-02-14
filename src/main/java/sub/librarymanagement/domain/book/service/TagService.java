package sub.librarymanagement.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sub.librarymanagement.common.exception.ApplicationException;
import sub.librarymanagement.common.exception.ErrorCode;
import sub.librarymanagement.persistence.book.entity.Tag;
import sub.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final BookRepository bookRepository;

    public TagIdDto registerTag(TagInfoDto tagInfoDto) {
        validateTagDuplication(tagInfoDto.getName());
        Tag tag = Tag.of(tagInfoDto.getName());
        bookRepository.saveTag(tag);
        return new TagIdDto().tagId(tag.getId());
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
        return new TagIdDto().tagId(tagId);
    }

    private void validateTagDeletion(Long tagId) {
        if (bookRepository.existsBookTagByTagId(tagId)) {
            throw new ApplicationException(ErrorCode.TAG_DELETION);
        }
    }

    public TagListDto getTagList() {
        List<TagDto> tagList = bookRepository.findAllTags()
                .stream()
                .map(tag -> new TagDto().tagId(tag.getId()).name(tag.getName()))
                .toList();
        return new TagListDto().tags(tagList);
    }
}
