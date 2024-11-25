package com.bteam.fantasychess_server.handler;

import com.bteam.fantasychess_server.service.PlayerService;
import com.bteam.fantasychess_server.service.TokenService;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.bteam.fantasychess_server.utils.UriUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

/**
 * {@link TextWebSocketHandler} extension, that maps the incoming methods to
 * the {@link WebSocketService}
 *
 * @author Marc
 */
public class TextWebSocketHandlerExt extends TextWebSocketHandler {
    final WebSocketService service;
    final TokenService tokenService;
    final PlayerService playerService;

    public TextWebSocketHandlerExt(WebSocketService service, TokenService tokenService, PlayerService playerService) {
        this.service = service;
        this.tokenService = tokenService;
        this.playerService = playerService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        var queryMap = UriUtils.getQueryParameters(Objects.requireNonNull(session.getUri()));
        var token = queryMap.get("token");
        var playerId = tokenService.getUUID(token);
        var player = playerService.getPlayer(playerId);
        tokenService.invalidateToken(token);
        service.registerSession(session,player);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        service.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        service.removeSession(session.getId(), status);
    }
}
