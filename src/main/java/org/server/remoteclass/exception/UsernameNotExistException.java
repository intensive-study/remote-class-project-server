package org.server.remoteclass.exception;

import lombok.Getter;

@Getter
public class UsernameNotExistException extends Throwable{
    private ErrorCode errorCode;

    public UsernameNotExistException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
