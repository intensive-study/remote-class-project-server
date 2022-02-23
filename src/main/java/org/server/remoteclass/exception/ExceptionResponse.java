package org.server.remoteclass.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private HttpStatus status;
    private String code;
    private String message;

    public ExceptionResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ExceptionResponse(String message, String code, HttpStatus status) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
