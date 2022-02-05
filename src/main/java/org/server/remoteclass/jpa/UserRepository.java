package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneWithAuthoritiesByName(String name);
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);
    Optional<User> findByUserId(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email); // 이메일 중복 체크시 사용 예정
}
