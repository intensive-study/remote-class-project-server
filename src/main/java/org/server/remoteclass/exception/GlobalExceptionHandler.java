package org.server.remoteclass.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    final String INVALID_ARGUMENT_ERROR_CODE = "ARGUMENT-NOT-VALID-ERR-404";

    @ExceptionHandler(RemoteGlobalException.class)
    public ResponseEntity<ExceptionResponse> handleRemoteException(RemoteGlobalException exception){
        log.error("RemoteGlobalException: {}", exception.getErrorCode().getMessage());
        ExceptionResponse response = new ExceptionResponse(exception.getErrorCode());
        return new ResponseEntity<>(response, exception.getErrorCode().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        Object target = exception.getBindingResult().getTarget();
        String responseMessage = String.format("%s: %s", target, errorMessage);
        log.error("MethodArgumentNotValidException, {}", responseMessage);
        ExceptionResponse response = new ExceptionResponse(responseMessage, INVALID_ARGUMENT_ERROR_CODE, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
