package sub.librarymanagement.domain.user.dto;

public record UserIdDto(
        Long userId
) {
    public static UserIdDto from(Long userId) {
        return new UserIdDto(userId);
    }
}
