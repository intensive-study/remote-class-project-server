package org.server.remoteclass.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private ErrorCode errorCode;

    public ForbiddenException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}