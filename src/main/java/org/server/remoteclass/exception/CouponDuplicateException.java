package org.server.remoteclass.exception;

public class CouponDuplicateException extends RemoteGlobalException {

    public CouponDuplicateException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
