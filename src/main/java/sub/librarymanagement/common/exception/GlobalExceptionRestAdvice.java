package sub.librarymanagement.common.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sub.librarymanagement.common.util.ResponseDto;

@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleApplicationException(ApplicationException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.error(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleBindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.errorWithMessage(
                        HttpStatus.BAD_REQUEST,
                        e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                ));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleDbException(DataAccessException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Void>> handleServerException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.errorWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}