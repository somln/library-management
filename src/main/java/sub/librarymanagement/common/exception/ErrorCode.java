package sub.librarymanagement.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),
    USERNAME_DUPLICATION(BAD_REQUEST, "중복된 사용자 이름입니다"),
    EMAIL_DUPLICATION(BAD_REQUEST, "중복된 이메일입니다"),
    INVALID_ROLE(BAD_REQUEST, "유효하지 않은 권한입니다"),
    AUTHORIZATION_HEADER_NOT_FOUND(BAD_REQUEST, "Authorization 헤더가 존재하지 않습니다"),
    TOKEN_EXPIRED(BAD_REQUEST, "토큰이 만료되었습니다"),
    INVALID_TOKEN(BAD_REQUEST, "유효하지 않은 토큰입니다"),
    BOOK_DUPLICATION(BAD_REQUEST, "중복된 책 정보입니다"),
    BOOK_NOT_FOUND(NOT_FOUND, "책을 찾을 수 없습니다"),
    CANNOT_DELETE_BOOK(BAD_REQUEST, "대출 중인 책은 삭제할 수 없습니다"),
    CANNOT_LOAN_BOOK(BAD_REQUEST, "대출 중인 책은 대출할 수 없습니다"),
    LOAN_NOT_FOUND(NOT_FOUND, "대출 정보를 찾을 수 없습니다"),
    ALREADY_RETURNED_LOAN(BAD_REQUEST, "이미 반납된 대출입니다"),
    CANNOT_RETURN_OTHER_USER_LOAN(BAD_REQUEST, "다른 사용자의 대출은 반납할 수 없습니다");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}