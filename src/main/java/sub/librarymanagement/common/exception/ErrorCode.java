package sub.librarymanagement.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "중복된 이메일입니다"),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 권한입니다");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}