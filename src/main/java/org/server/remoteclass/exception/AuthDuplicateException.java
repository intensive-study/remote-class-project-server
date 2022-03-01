package org.server.remoteclass.exception;

public class AuthDuplicateException extends RemoteGlobalException{

    public AuthDuplicateException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
