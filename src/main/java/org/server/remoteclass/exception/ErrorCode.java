package org.server.remoteclass.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_DUPLICATION(HttpStatus.CONFLICT,"NAME-DUPLICATED-ERR-409","이미 가입된 이메일입니다."),
    AUTH_DUPLICATION(HttpStatus.CONFLICT, "AUTH-DUPLICATED-ERR-409", "이미 해당 권한을 가지고 있습니다"),
    COUPON_DUPLICATION(HttpStatus.CONFLICT, "COUPON-DUPLICATED-ERR-409", "이미 발급 받은 쿠폰입니다."),
    STATUS_DUPLICATION(HttpStatus.CONFLICT, "STATUS-DUPLICATED-ERR-409", "쿠폰 상태가 이미 요청 상태와 같습니다."),
    ID_NOT_EXIST(HttpStatus.NOT_FOUND,"ID-NOT-EXIST-ERR-404","해당 ID를 가진 데이터가 없습니다."),
    USERNAME_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR,"USERNAME-NOT-EXIST-ERR-500","해당 USERNAME 가진 데이터가 없습니다."),
    NON_AUTHORITATIVE_INFORMATION(HttpStatus.NON_AUTHORITATIVE_INFORMATION, "NON-AUTHORITATIVE-ERR-203", "수정 / 삭제 권한이 없습니다."),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "NOT-ACCEPTABLE-QUERY-ERR-406", "해당 명령을 수행할 수 없습니다. (SOL 수행 실패)"),
    BAD_REQUEST_ARGUMENT(HttpStatus.BAD_REQUEST, "BAD_REQUEST_ARGUMENT-400", "메소드 호출 시 전달 값이 올바르지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "NO-RELEVANT-PERMISSIONS-ERR-403", "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED-401", "인증이 유효하지 않습니다.");
    private HttpStatus status;
    private String code;
    private String message;
}
