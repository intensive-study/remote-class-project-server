package org.server.remoteclass.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RemoteGlobalException.class)
    public ResponseEntity<ExceptionResponse> handleRemoteException(RemoteGlobalException exception){
        log.error("RemoteGlobalException", exception.getErrorCode().getMessage());
        ExceptionResponse response = new ExceptionResponse(exception.getErrorCode());
        return new ResponseEntity<>(response, exception.getErrorCode().getStatus());
    }
}
