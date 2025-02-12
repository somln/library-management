package sub.librarymanagement.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),
    USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "중복된 사용자 이름입니다"),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "중복된 이메일입니다"),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 권한입니다"),
    AUTHORIZATION_HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Authorization 헤더가 존재하지 않습니다"),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}