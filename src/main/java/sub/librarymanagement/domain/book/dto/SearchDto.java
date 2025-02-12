package sub.librarymanagement.domain.book.dto;

import jakarta.validation.constraints.NotBlank;

public record SearchDto (
        @NotBlank(message = "검색어를 입력해주세요")
        String q
){

}
