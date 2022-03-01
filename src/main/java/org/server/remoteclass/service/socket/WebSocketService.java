package org.server.remoteclass.service.socket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.dto.socket.LiveRequestDto;
import org.server.remoteclass.jpa.socket.SessionRepository;
import org.server.remoteclass.service.lecture.LectureService;
import org.server.remoteclass.service.student.StudentService;
import org.server.remoteclass.util.GsonUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.server.remoteclass.util.EncryptString.*;


@Slf4j
@Transactional
public class WebSocketService {

    private final SessionRepository sessionRepository;
    private final TemplateForSynchronized template;
    private final StudentService studentService;
    private final LectureService lectureService;

    public WebSocketService(SessionRepository sessionRepository,
                            TemplateForSynchronized templateForSynchronized,
                            StudentService studentService,
                            LectureService lectureService
    ) {
        this.sessionRepository = sessionRepository;
        this.template = templateForSynchronized;
        this.studentService = studentService;
        this.lectureService = lectureService;
    }

    @Transactional(readOnly = true)
    public void isLiveProceeding(WebSocket socket, LiveRequestDto messageObj) {
        boolean proceeding = sessionRepository.containsLectureSessionOnSessionManager(changeLongToString(messageObj.lectureId));

        Map<String, Object> objectMap = GsonUtil.makeCommonMap("isLiveProceeding", messageObj.userId, 200);
        objectMap.put("proceeding", proceeding);
        GsonUtil.commonSendMessage(socket, objectMap);

        log.info("라이브 진행 여부 발송 성공 proceeding: {}",proceeding);
    }

    public void enterWaitingRoom(WebSocket socket, LiveRequestDto messageObj) {
        validateAccessPermission(messageObj.lectureId, messageObj.userId);
        ResponseLectureDto lecture = lectureService.getLectureByLectureId(messageObj.lectureId);

        NeedToSynchronized executingLogic = () -> {
            // SessionManager에 해당 강의 Id가 키로 있는지 조사(요청 시점에 강의가 생성되었을 수도 있으니)
            Boolean lectureProceeding = sessionRepository.containsLectureSessionOnSessionManager(changeLongToString(lecture.getLectureId()));
            log.info("proceeding: {}",lectureProceeding);
            // 있는 경우, 이미 강의 라이브 진행중 (요청 시점에 라이브 실행됨)
            // 컬렉션에 커넥션 추가할 필요없이 강의가 생성되었음을 알림
            if (lectureProceeding){
                Map<String, Object> objectMap = GsonUtil.makeCommonMap("liveStarted", messageObj.userId, 200);
                objectMap.put("lecturerId", lecture.getLecturer());
                GsonUtil.commonSendMessage(socket, objectMap);
                log.info("강의 열림: {}",messageObj.lectureId);

            } else{
                // 없는 경우
                // waiting room 이 없으면 생성
                String changedLectureId = changeLongToString(lecture.getLectureId());
                if(!sessionRepository.containsKeyOnWaitingRoom(changedLectureId))
                    sessionRepository.createWaitingRoomByLectureId(changedLectureId);
                // 해당 컬렉션에 본인 커넥션 추가
                sessionRepository.addConnectionOnWaitingRoom(changedLectureId, socket);
                Map<String, Object> objectMap = GsonUtil.makeCommonMap("enterWaitingRoom", messageObj.userId, 200);
                objectMap.put("lecturerId", lecture.getLecturer());
                GsonUtil.commonSendMessage(socket, objectMap);
                log.info("대기실 입장: {}",messageObj.userId);
            }
        };

        template.executeToSynchronize(executingLogic);

    }

    public void startLive(WebSocket socket, LiveRequestDto messageObj) {
        // 해당 멤버가 lecture 관련 회원인지 검증(강의자, 수강자)
        validateLecturer(messageObj.lectureId, messageObj.userId);

        NeedToSynchronized executingLogic = () -> {
            startLiveLecture(messageObj.lectureId, messageObj.userId, socket);

            Map<String, Object> objectMap = GsonUtil.makeCommonMap("startLive", messageObj.userId, 200);
            GsonUtil.commonSendMessage(socket, objectMap);
            log.info("라이브 생성 성공, {}", socket.getRemoteSocketAddress());

            // 대기실에 있는 모든 클라이언트에게 강의 생성 알림
            if(sessionRepository.containsKeyOnWaitingRoom(changeLongToString(messageObj.lectureId))) {
                List<WebSocket> connections = sessionRepository.getConnectionsOnWaitingRoom(changeLongToString(messageObj.lectureId));
                for (WebSocket connection : connections) {
                    Map<String, Object> map = GsonUtil.makeCommonMap("liveStarted", 0L, 200);
                    map.put("lecturerId", messageObj.userId);
                    GsonUtil.commonSendMessage(connection, map);
                }
                // 강의가 열린후, 해당 강의의 대기실 삭제
                sessionRepository.removeKeyOnWaitingRoom(changeLongToString(messageObj.lectureId));
                log.info("liveStarted, 알림 발송 성공");

            }
        };
        template.executeToSynchronize(executingLogic);

    }

    public void enterLive(WebSocket socket, LiveRequestDto messageObj) {
        validateAccessPermission(messageObj.lectureId, messageObj.userId);

        enterLiveLecture(messageObj.lectureId, messageObj.userId, socket);

        Map<String, Object> objectMap = GsonUtil.makeCommonMap("enterLive", messageObj.userId, 200);
        GsonUtil.commonSendMessage(socket, objectMap);

        log.info("라이브 입장 성공, {}", socket.getRemoteSocketAddress());

        // 입장 발송
        Map<String, Object> map = GsonUtil.makeCommonMap("userEntered", messageObj.userId, 200);
        sendToAll(messageObj.lectureId, messageObj.userId, map);
    }

    public void getConnectionsOnLecture(WebSocket socket, LiveRequestDto messageObj) {
        validateAccessPermission(messageObj.lectureId, messageObj.userId);

        List<String> connections = sessionRepository.getConnectionsByLectureId(changeLongToString(messageObj.lectureId));
        List<Long> membersInLecture = new ArrayList<>();
        for (String connection : connections) {
            Long decrypted = decrypt(connection);
            if (decrypted == null) continue;
            membersInLecture.add(decrypted);
        }

        Map<String, Object> objectMap = GsonUtil.makeCommonMap("getConnectionsOnLecture", messageObj.userId, 200);
        objectMap.put("members", membersInLecture);
        GsonUtil.commonSendMessage(socket, objectMap);

        log.info("접속자 전송 성공, {}", socket.getRemoteSocketAddress());

    }

    @Transactional(readOnly = true)
    public void iceCandidate(WebSocket socket, LiveRequestDto messageObj) {

        // 본인을 제외한 나머지 참여자에게 offer 혹은 answer 전달
        Map<String, Object> map = GsonUtil.makeCommonMap("iceCandidate", messageObj.userId, 200);
        map.put("candidate", messageObj.candidate);
        sendToAll(messageObj.lectureId, messageObj.userId, map);

        // sdp 전체 발송 성공
        Map<String, Object> objectMap = GsonUtil.makeCommonMap("iceCandidate", messageObj.userId, 200);
        GsonUtil.commonSendMessage(socket, objectMap);

    }


    @Transactional(readOnly = true)
    public void sdp(WebSocket socket, LiveRequestDto messageObj) {
        // 본인을 제외한 나머지 참여자에게 offer 혹은 answer 전달
        Map<String, Object> map = GsonUtil.makeCommonMap("sdp", messageObj.userId, 200);
        map.put("spd", messageObj.sdp);
        sendToAll(messageObj.lectureId, messageObj.userId,  map);

        // sdp 전체 발송 성공
        Map<String, Object> objectMap = GsonUtil.makeCommonMap("sdp", messageObj.userId, 200);
        GsonUtil.commonSendMessage(socket, objectMap);
    }


    public void exitLive(WebSocket socket, LiveRequestDto messageObj) {
        validateAccessPermission(messageObj.lectureId, messageObj.userId);

        // 강사의 강의 종료
        // 강사와 요청 멤버가 동일한 인물인지 검증
        if(lectureService.checkIfUserIsLecturerInLecture(messageObj.lectureId, messageObj.userId)) {
            // sessionManager 돌면서 현재 session에 참여하고 있는 user 탐색
            String lectureToString = changeLongToString(messageObj.lectureId);
            for (String needToRemove : sessionRepository.getConnectionsByLectureId(lectureToString)) {
                removeConnections(needToRemove);
            }
            sessionRepository.removeLectureSessionByLectureId(lectureToString);
        }
        // 수강생 강의 종료
        else {
            String encryptedUser = changeLongToString(messageObj.lectureId, messageObj.userId);
            if(!sessionRepository.containsConnectionOnLectureSession(changeLongToString(messageObj.lectureId), encryptedUser))
                throw new IllegalArgumentException("접속 정보가 없는 사용자입니다.");
            removeConnections(encryptedUser);
            sessionRepository.removeConnectionOnLectureSession(changeLongToString(messageObj.lectureId), encryptedUser);

            Map<String, Object> mapObj = GsonUtil.makeCommonMap("userExited", messageObj.userId, 200);
            sendToAll(messageObj.lectureId, messageObj.userId,  mapObj);
        }
        log.info("client exited: {}", messageObj.userId);

        Map<String, Object> objectMap = GsonUtil.makeCommonMap("exitLive", messageObj.userId, 200);
        GsonUtil.commonSendMessage(socket, objectMap);

    }


    private void sendToAll(Long lectureId, Long userId, Map<String, Object> objectMap) {
        String changedValue = changeLongToString(lectureId, userId);
        for (String target : sessionRepository.getConnectionsByLectureId(changeLongToString(lectureId))) {
            // 본인을 제외한 모두에게 요청보냄, 컨넥션이 없을 수도 있으니 target 값의 존재 여부 조사 후 전송
            if (!target.equals(changedValue) && sessionRepository.containsKeyOnConnections(target))
                GsonUtil.commonSendMessage(sessionRepository.getWebSocketOnConnections(target), objectMap);
        }
    }

    private void startLiveLecture(Long lectureId, Long userId, WebSocket conn) {
        String lectureToString = changeLongToString(lectureId);
        String changedValue = changeLongToString(lectureId, userId);
        sessionRepository.addConnectionOnLectureSession(lectureToString, changedValue);
        sessionRepository.addWebSocketOnConnections(changedValue, conn);

        log.info("강의 세션이 생성되었습니다.");
    }

    private void enterLiveLecture(Long lectureId, Long userId, WebSocket conn) {
        String lectureToString = changeLongToString(lectureId);
        if(!sessionRepository.containsLectureSessionOnSessionManager(lectureToString)) throw new IllegalArgumentException("현재 진행하지 않는 강의입니다.");

        String changedValue = changeLongToString(lectureId, userId);
        sessionRepository.addConnectionOnLectureSession(lectureToString, changedValue);
        sessionRepository.addWebSocketOnConnections(changedValue, conn);
        log.info("강의 세션에 참가하였습니다");
    }

    private void removeConnections(String target) {
        if(sessionRepository.containsKeyOnConnections(target)){
            sessionRepository.closeConnection(target);
            sessionRepository.removeKeyOnConnections(target);
        }
    }

    // 권한 체크

    private void validateAccessPermission(Long lectureId, Long userId) {
        boolean isStudent = studentService.checkIfUserIsStudentInLecture(lectureId, userId);
        boolean isLecturer = lectureService.checkIfUserIsLecturerInLecture(lectureId, userId);
        if (!isStudent && !isLecturer) throw new IllegalArgumentException("권한이 없는 사용자입니다.");
    }

    private void validateLecturer(Long lectureId, Long userId) {
        boolean isLecturer = lectureService.checkIfUserIsLecturerInLecture(lectureId, userId);
        if (!isLecturer) throw new IllegalArgumentException("권한이 없는 사용자입니다.");
    }

}
