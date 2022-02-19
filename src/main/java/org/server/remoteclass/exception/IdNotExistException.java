package org.server.remoteclass.exception;

import lombok.Getter;

@Getter
public class IdNotExistException extends RuntimeException {
    private ErrorCode errorCode;

    public IdNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
