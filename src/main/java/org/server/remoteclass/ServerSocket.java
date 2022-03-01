package org.server.remoteclass;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.server.remoteclass.dto.socket.LiveRequestDto;
import org.server.remoteclass.service.socket.WebSocketService;
import org.server.remoteclass.util.GsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ServerSocket extends WebSocketServer {

    private Map<String, Method> methodMap = new HashMap<>();
    private WebSocketService webSocketService;

    public ServerSocket() {
        super(new InetSocketAddress(8888));
        log.info("소켓 시작");
    }

    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void setMethodMap(Map<String, Method> methodMap) {
        this.methodMap = methodMap;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        log.info("New client connected: " + conn.getRemoteSocketAddress() + " hash " + conn.getRemoteSocketAddress().hashCode());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            log.info(message);

            // Gson 객체 생성
            Gson gson = new Gson();
            LiveRequestDto messageObj = gson.fromJson(message, LiveRequestDto.class);
            // start, enter, exit, sdp
            methodMap.get((messageObj.type).toLowerCase()).invoke(webSocketService, conn, messageObj);

        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            Map<String, Object> objectMap = GsonUtil.makeCommonMap(Arrays.toString(e.getStackTrace()), -1L, 400);
            GsonUtil.commonSendMessage(conn, objectMap);

            log.info(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        log.info("client disconnected (code: {})", code);
    }

    @Override
    public void onError(WebSocket conn, Exception exc) {
        System.out.println("Error happened: " + exc);
    }

}