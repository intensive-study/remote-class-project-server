package org.server.remoteclass.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<ExceptionResponse> handleEmailDuplicateException(EmailDuplicateException exception){
        log.error("handleEmailDuplicateException", exception);
        ExceptionResponse response = new ExceptionResponse(exception.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IdNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleIdNotExist(IdNotExistException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
