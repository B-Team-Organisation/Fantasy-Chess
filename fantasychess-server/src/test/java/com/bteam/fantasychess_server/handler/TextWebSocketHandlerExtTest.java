package com.bteam.fantasychess_server.handler;

import com.bteam.fantasychess_server.service.WebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TextWebSocketHandlerExtTest {

    private static final String TEST_ID = "TEST_ID";
    private final CloseStatus closeStatus = CloseStatus.NORMAL;

    @InjectMocks
    private TextWebSocketHandlerExt handler;

    @Mock
    private WebSocketSession session;

    @Mock
    private TextMessage textMessage;

    @Mock
    private WebSocketService webSocketService;

    @BeforeEach
    void setUp() {
        session = mock(WebSocketSession.class);
        webSocketService = mock(WebSocketService.class);
        handler = new TextWebSocketHandlerExt(webSocketService);
    }

    @Test
    void afterConnectionEstablished() throws Exception {
        when(session.getId()).thenReturn(TEST_ID);

        handler.afterConnectionEstablished(session);

        verify(webSocketService, times(1)).registerSession(any());
    }

    @Test
    void handleTextMessage() throws Exception {
        when(session.getId()).thenReturn(TEST_ID);

        handler.handleTextMessage(session, textMessage);

        verify(webSocketService, times(1)).handleTextMessage(any(), any());
    }

    @Test
    void afterConnectionClosed() throws Exception {
        when(session.getId()).thenReturn(TEST_ID);

        handler.afterConnectionClosed(session, closeStatus);

        verify(webSocketService, times(1)).removeSession(any(), any());
    }
}