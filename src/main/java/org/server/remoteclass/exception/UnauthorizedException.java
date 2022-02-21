package org.server.remoteclass.exception;


public class UnauthorizedException extends RemoteGlobalException {

    public UnauthorizedException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}