package org.server.remoteclass.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name="email", nullable = false, length = 50)
    private String email;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING) // Enumerated로 느려지는 단점이 있는데, 공부 후에 개선하겠습니다.
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    // 자료형 LocalDateTime과 호환 고려해 볼게요
    @Column(name="register_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp registerDate;
}
