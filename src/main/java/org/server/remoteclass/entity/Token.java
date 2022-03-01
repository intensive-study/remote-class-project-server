package org.server.remoteclass.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// 추후에 사용 할 예정입니다. 토큰 리프레쉬 관련 테이블입니다. redis 등도 고려해 봐요.
@Entity
@Data
@NoArgsConstructor
public class Token {

    @Id
    @Column(name = "key_name") // key가 예약어라 테이블 자동 생성이 안되서 key_name으로 변경
    private String key;
//    private Long key;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name="refresh_token")
    private String refreshToken;

    public Token updateValue(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        return this;
    }

    @Builder
    public Token(String key, String accessToken, String refreshToken){
        this.key = key;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
