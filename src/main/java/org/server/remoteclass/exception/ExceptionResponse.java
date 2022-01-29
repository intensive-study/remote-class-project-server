package org.server.remoteclass.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private HttpStatus status;
    private String code;
    private String message;

    public ExceptionResponse(ResultCode resultCode){
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}
