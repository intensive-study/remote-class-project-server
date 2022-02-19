package org.server.remoteclass.exception;


public class IdNotExistException extends RemoteGlobalException {

    public IdNotExistException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
