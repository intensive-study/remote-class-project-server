package org.server.remoteclass.exception;

import lombok.Getter;

@Getter
public class UsernameNotExistException extends Throwable{
    private ResultCode resultCode;

    public UsernameNotExistException(String message, ResultCode resultCode){
        super(message);
        this.resultCode = resultCode;
    }
}
