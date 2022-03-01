package org.server.remoteclass.jpa.socket;

import org.java_websocket.WebSocket;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract public class SessionManagerRepository implements SessionRepository{

    //lecture id, waiting session participants
    private final ConcurrentMap<String, List<WebSocket>> waitingRoom = new ConcurrentHashMap<>();

    // member id, member socket
    private final ConcurrentMap<String, WebSocket> connections = new ConcurrentHashMap<>();

    // connection
    public Boolean containsKeyOnConnections(String key) {
        return connections.containsKey(key);
    }

    public void closeConnection(String key) {
        connections.get(key).close();
    }

    public WebSocket getWebSocketOnConnections(String key) {
        return connections.get(key);
    }

    public void removeKeyOnConnections(String key) {
        connections.remove(key);
    }

    public void addWebSocketOnConnections(String key, WebSocket socket) {
        connections.put(key, socket);
    }


    // waiting room
    public Boolean containsKeyOnWaitingRoom(String key) {
        return waitingRoom.containsKey(key);
    }

    public void addConnectionOnWaitingRoom(String key, WebSocket connection) {
        waitingRoom.get(key).add(connection);
    }

    public List<WebSocket> getConnectionsOnWaitingRoom(String key) {
        return waitingRoom.get(key);
    }

    public void removeKeyOnWaitingRoom(String key) {
        waitingRoom.remove(key);
    }

    public void createWaitingRoomByLectureId(String key) {
        waitingRoom.put(key, new LinkedList<>());
    }


    @Override
    abstract public Boolean containsLectureSessionOnSessionManager(String key);

    @Override
    abstract public Boolean containsConnectionOnLectureSession(String lectureId, String key);

    @Override
    abstract public List<String> getConnectionsByLectureId(String lectureId);

    @Override
    abstract public void removeLectureSessionByLectureId(String lectureId);

    @Override
    abstract public void removeConnectionOnLectureSession(String lectureId, String targetToRemove);

    @Override
    abstract public void addConnectionOnLectureSession(String lectureId, String targetToAdd);
}
