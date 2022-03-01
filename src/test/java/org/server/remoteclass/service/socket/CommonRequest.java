package org.server.remoteclass.service.socket;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.server.remoteclass.dto.socket.LiveRequestDto;
import org.server.remoteclass.jpa.socket.SessionRepository;
import org.server.remoteclass.service.lecture.LectureService;
import org.server.remoteclass.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class CommonRequest {
    WebSocketClient client;
    StudentService studentService;
    LectureService lectureService;
    SessionRepository sessionRepository;
    WebSocketService webSocketService;
    TemplateForSynchronized template;

    public CommonRequest(StudentService studentService,
                         LectureService lectureService,
                         SessionRepository sessionRepository) {
        client = new ClientSocketStub(URI.create("ws://localhost:8888/"));

        this.studentService = studentService;
        this.lectureService = lectureService;
        this.sessionRepository = sessionRepository;
        template = new TemplateForSynchronized();
        webSocketService = new WebSocketService(sessionRepository, template, studentService, lectureService);
    }

    @Autowired
    public CommonRequest(StudentService studentService,
                         LectureService lectureService,
                         SessionRepository sessionRepository,
                         TemplateForSynchronized templateForSynchronized,
                         WebSocketService webSocketService) {

        client = new ClientSocketStub(URI.create("ws://localhost:8888/"));
        this.studentService = studentService;
        this.lectureService = lectureService;
        this.sessionRepository = sessionRepository;
        this.template = templateForSynchronized;
        this.webSocketService = webSocketService;
    }

    WebSocket getConnection() {
        return client.getConnection();
    }

    public void startLive(Long memberId, Long lectureId) {
        LiveRequestDto startLive = LiveRequestDto.buildBasicDto("startLive", memberId, lectureId, null);
        webSocketService.startLive(client.getConnection(), startLive);
    }

    public void enterWaitingRoom(Long memberId, Long lectureId) {
        LiveRequestDto enterWaitingRoom = LiveRequestDto.buildBasicDto("enterWaitingRoom", memberId, lectureId, null);
        webSocketService.enterWaitingRoom(client.getConnection(), enterWaitingRoom);
    }

    public void enterLive(Long memberId, Long lectureId) {
        LiveRequestDto enterLive = LiveRequestDto.buildBasicDto("enterWaitingRoom", memberId, lectureId, null);
        client = new ClientSocketStub(URI.create("ws://localhost:8888/"));
        webSocketService.enterLive(client.getConnection(), enterLive);
    }


    public void exitLive(Long memberId, Long lectureId) {
        LiveRequestDto exitLive = LiveRequestDto.buildBasicDto("exitLive", memberId, lectureId, null);
        webSocketService.exitLive(client.getConnection(), exitLive);
    }

    public void isLiveProceeding(Long memberId, Long lectureId) {
        LiveRequestDto isLiveProceeding = LiveRequestDto.buildBasicDto("isLiveProceeding", memberId, lectureId, null);
        webSocketService.isLiveProceeding(client.getConnection(), isLiveProceeding);
    }


}
