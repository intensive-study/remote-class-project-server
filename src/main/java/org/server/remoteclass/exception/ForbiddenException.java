package org.server.remoteclass.exception;


public class ForbiddenException extends RemoteGlobalException {

    public ForbiddenException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}