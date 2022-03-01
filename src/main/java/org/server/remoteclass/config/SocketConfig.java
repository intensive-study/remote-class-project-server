package org.server.remoteclass.config;

import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.ServerSocket;
import org.server.remoteclass.jpa.socket.RedisSessionRepository;
import org.server.remoteclass.jpa.socket.SessionRepository;
import org.server.remoteclass.service.lecture.LectureService;
import org.server.remoteclass.service.socket.TemplateForSynchronized;
import org.server.remoteclass.service.socket.WebSocketService;
import org.server.remoteclass.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class SocketConfig {
    @Autowired private RedisTemplate<String, String> redisTemplate;
    @Autowired private StudentService studentService;
    @Autowired private LectureService lectureService;

    @Bean
    public ServerSocket lectureSession() {
        return new ServerSocket();
    }

    @Bean
    WebSocketService webSocketService() {
        return new WebSocketService(sessionRepository(), templateForSynchronized(), studentService, lectureService);
    }

    @Bean
    TemplateForSynchronized templateForSynchronized() {
        return new TemplateForSynchronized();
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new RedisSessionRepository(redisTemplate);
    }

    @PostConstruct
    public void startLectureSession() {
        this.lectureSession().start();
        this.lectureSession().setWebSocketService(webSocketService());
        this.lectureSession().setMethodMap(lectureSessionInit());
        log.info("소켓 서버가 시작됩니다. port: {}", this.lectureSession().getPort());
    }

    private Map<String, Method> lectureSessionInit() {
        Map<String, Method> methodMap = new HashMap<>();
        Method[] methods = WebSocketService.class.getMethods();

        for (Method method : methods) {
            methodMap.put(method.getName().toLowerCase(), method);
        }

        return methodMap;
    }
}
