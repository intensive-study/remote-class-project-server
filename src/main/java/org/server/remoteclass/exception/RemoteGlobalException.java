package org.server.remoteclass.exception;

import lombok.Getter;

@Getter
public class RemoteGlobalException extends RuntimeException {
    private ErrorCode errorCode;

    public RemoteGlobalException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
