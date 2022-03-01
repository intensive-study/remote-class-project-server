package org.server.remoteclass.jpa.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class RedisSessionRepository extends SessionManagerRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // lecture id, session participants
    private final SetOperations<String, String> sessionManager;

    public RedisSessionRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.sessionManager = redisTemplate.opsForSet();
    }

    // sessionManager
    /**
     * sessionManager.size(key) 해당 키값으로 저장된 값이 없는 경우, 0 반환
     * @return
     */
    @Override
    public Boolean containsLectureSessionOnSessionManager(String key) {
        return sessionManager.size(key) != 0;
    }

    /**
     * sessionManager.members(lectureId)
     * 해당 키 값으로 반환 값 없을 경우, return []
     */
    @Override
    public Boolean containsConnectionOnLectureSession(String lectureId, String key) {
        if(sessionManager.members(lectureId) == null) return false;
        return Objects.requireNonNull(sessionManager.members(lectureId)).contains(key);
    }

    @Override
    public List<String> getConnectionsByLectureId(String lectureId) {
        return new ArrayList<>(sessionManager.members(lectureId));
    }

    @Override
    public void removeLectureSessionByLectureId(String lectureId) {
        redisTemplate.delete(lectureId);
    }

    @Override
    public void removeConnectionOnLectureSession(String lectureId, String targetToRemove) {
        sessionManager.remove(lectureId, targetToRemove);
    }

    @Override
    public void addConnectionOnLectureSession(String lectureId, String targetToAdd) {
        sessionManager.add(lectureId, targetToAdd);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clearAll() {
        if (log.isDebugEnabled())
            log.debug("모든 캐시를 삭제합니다...");
        try {
            redisTemplate.execute((RedisCallback) connection -> {
                connection.flushAll();
                return null;
            });
        } catch (Exception e) {
            log.warn("모든 캐시를 삭제하는데 실패했습니다.", e);
        }
    }

}
