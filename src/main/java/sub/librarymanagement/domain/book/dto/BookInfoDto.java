package sub.librarymanagement.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record BookInfoDto(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        @NotBlank(message = "저자는 필수입니다.")
        String author,
        @NotBlank(message = "출판사는 필수입니다.")
        String publisher,
        @NotNull(message = "출판일은 필수입니다.")
        LocalDate publishDate,

        List<Long> tagIds
) {
    public List<Long> tagIds() {
        return tagIds == null ? Collections.emptyList() : tagIds;
    }
}
