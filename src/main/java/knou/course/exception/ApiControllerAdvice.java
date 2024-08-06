package knou.course.exception;

import knou.course.dto.ApiResponse;
import knou.course.swagger.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<Object>> handleException(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ApiResponse.of(
                        e.getErrorCode().getStatus(),
                        e.getMessage(),
                        null
                ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BindException.class)
//    public ErrorResponseDto bindException(BindException e) {
//        return new ErrorResponseDto(
//                HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.name(),
//                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
//        );
//    }
}
