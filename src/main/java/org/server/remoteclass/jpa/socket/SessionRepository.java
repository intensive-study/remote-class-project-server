package org.server.remoteclass.jpa.socket;

import org.java_websocket.WebSocket;

import java.util.List;

public interface SessionRepository {
    // connection
    Boolean containsKeyOnConnections(String key);
    WebSocket getWebSocketOnConnections(String key);
    void removeKeyOnConnections(String key);
    void addWebSocketOnConnections(String key, WebSocket socket);
    void closeConnection(String key);

    // sessionManager
    Boolean containsLectureSessionOnSessionManager(String key);
    Boolean containsConnectionOnLectureSession(String lectureId, String key);
    List<String> getConnectionsByLectureId(String lectureId);
    void removeLectureSessionByLectureId(String lectureId);
    void removeConnectionOnLectureSession(String lectureId, String targetToRemove);
    void addConnectionOnLectureSession(String lectureId, String targetToAdd);
    void clearAll();

    // waitingRoom
    Boolean containsKeyOnWaitingRoom(String changeLongToString);
    void addConnectionOnWaitingRoom(String changeLongToString, WebSocket socket);
    List<WebSocket> getConnectionsOnWaitingRoom(String changeLongToString);
    void removeKeyOnWaitingRoom(String changeLongToString);
    void createWaitingRoomByLectureId(String changeLongToString);

}
