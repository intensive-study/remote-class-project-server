package org.server.remoteclass.jpa;

import org.server.remoteclass.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByKey(String key);
}
