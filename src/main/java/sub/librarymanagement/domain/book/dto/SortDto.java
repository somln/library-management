package sub.librarymanagement.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SortDto(
        @NotBlank(message = "sortBy는 필수입니다.")
        @Pattern(regexp = "title|publishDate", message = "sortBy는 title 또는 publishDate만 가능합니다.")
        String sortBy,

        @Pattern(regexp = "asc|desc", message = "sortDirection은 asc 또는 desc만 가능합니다.")
        String sortDirection
) {
        public String sortDirection() {
                return sortDirection == null ? "asc" : sortDirection;
        }
}
