package com.bteam.fantasychess_server.config;

import com.bteam.fantasychess_server.service.WebSocketService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class TextWebSocketHandlerExt extends TextWebSocketHandler {
    WebSocketService service;

    public TextWebSocketHandlerExt(WebSocketService service){
        this.service = service;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("Connected");
        service.registerSession(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }
}
