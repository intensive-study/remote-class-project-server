package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneWithAuthoritiesByName(String name);
    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email); // 이메일 중복 체크시 사용 예정
}
