package sub.librarymanagement.domain.book.dto;

import java.util.List;

public record BookTagListDto (
        List<Long> tags
){
}
