package org.server.remoteclass.exception;


public class UsernameNotExistException extends RemoteGlobalException {

    public UsernameNotExistException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
