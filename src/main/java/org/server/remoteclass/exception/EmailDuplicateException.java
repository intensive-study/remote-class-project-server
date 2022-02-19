package org.server.remoteclass.exception;


public class EmailDuplicateException extends RemoteGlobalException {

    public EmailDuplicateException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
