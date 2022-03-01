package org.server.remoteclass.service.socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@ClientEndpoint
public class ClientSocketStub extends WebSocketClient {

    public ClientSocketStub(URI endpointURI) {
        super(endpointURI);
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("handshakedata.getHttpStatus() = " + handshakedata.getHttpStatus());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("message = " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("reason = " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("ex.getMessage() = " + ex.getMessage());
    }

}