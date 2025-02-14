package sub.librarymanagement.common.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sub.model.ResponseDtoError;

@RestControllerAdvice
public class GlobalExceptionRestAdvice {

    @ExceptionHandler
    public ResponseEntity<ResponseDtoError> handleApplicationException(ApplicationException e) {
        ResponseDtoError errorResponse = new ResponseDtoError()
                .code(e.getErrorCode().getHttpStatus().value())
                .errorMessage(e.getErrorCode().getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDtoError> handleBindException(BindException e) {
        ResponseDtoError errorResponse = new ResponseDtoError()
                .code(HttpStatus.BAD_REQUEST.value())
                .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDtoError> handleDbException(DataAccessException e) {
        ResponseDtoError errorResponse = new ResponseDtoError()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDtoError> handleServerException(RuntimeException e) {
        ResponseDtoError errorResponse = new ResponseDtoError()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}