package org.server.remoteclass.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends Exception{
    private ResultCode resultCode;

    public ForbiddenException(String message, ResultCode resultCode){
        super(message);
        this.resultCode = resultCode;
    }
}