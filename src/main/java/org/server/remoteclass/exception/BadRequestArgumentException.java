package org.server.remoteclass.exception;


public class BadRequestArgumentException extends RemoteGlobalException {

    public BadRequestArgumentException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
