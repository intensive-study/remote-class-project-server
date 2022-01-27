package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findOneWithAuthoritiesByUserEmail(String userEmail);
    Optional<UserEntity> findByUserEmail(String userEmail);
}
