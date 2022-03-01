package org.server.remoteclass.exception;

public class StatusDuplicateException extends RemoteGlobalException {

    public StatusDuplicateException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
