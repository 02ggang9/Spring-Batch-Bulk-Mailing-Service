package io.springbatch.springbatch.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.internalServerError()
                .body("알 수 없는 오류" + e);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> businessException(Exception e) {
        return ResponseEntity.internalServerError()
                .body("Business Exception: " + e);
    }
}
