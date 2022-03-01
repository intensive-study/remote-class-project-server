package org.server.remoteclass.util;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.util.HashMap;
import java.util.Map;

public class GsonUtil{

    private static final Gson gson = new Gson();

    public static <T> T decode(String s, Class<T> type){
        if(s == null) return null;
        return gson.fromJson(s, type);
    }

    public static <T> String encode(T request){
        if(request == null) return "";
        return gson.toJson(request);
    }

    public static Map<String, Object> makeCommonMap(String type, Long userId, int status) {

        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("userId", userId);
        map.put("status", status);

        return map;
    }

    public static void commonSendMessage(WebSocket socket, Map<String, Object> keyValue) {
        try {
            socket.send(encode(keyValue));
        } catch (WebsocketNotConnectedException e) {
            System.out.println("e = " + e);
        }
    }

}
