package com.bteam.fantasychess_server.client;

import com.bteam.common.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientTest {
    private static final String TEST_ID = "TEST_ID";

    Client client;

    @Mock
    WebSocketSession session;

    Player player = new Player("","",new ArrayList<>());

    @BeforeEach
    void setUp() {
        session = mock(WebSocketSession.class);
        client = new Client(TEST_ID, session, player);
    }

    @Test
    void getId() {
        when(session.getId()).thenReturn(TEST_ID);
        assertEquals(TEST_ID, client.getId());
    }

    @Test
    void getSession() {
        assertEquals(session, client.getSession());
    }

    @Test
    void getOnMessageReceivedEvent() {
        AtomicInteger calls = new AtomicInteger(0);
        client.getOnMessageReceivedEvent().addListener(c -> {
            assertEquals("TEST", c);
            calls.incrementAndGet();
        });
        client.getOnMessageReceivedEvent().invoke("TEST");
        assertEquals(1, calls.get());

    }

    @Test
    void sendMessageSuccess() throws IOException {
        var result = client.sendMessage(new TextMessage("TEST"));
        verify(session).sendMessage(any());
        assertTrue(result.isSuccess());
    }

    @Test
    void sendMessageFailure() throws IOException {
        var message = new TextMessage("TEST");
        doThrow(new IOException()).when(session).sendMessage(any());
        var result = client.sendMessage(message);
        assertFalse(result.isSuccess());
    }

    @Test
    void onClientDisconnected() {
        AtomicInteger calls = new AtomicInteger(0);
        client.getOnClientDisconnected().addListener(c -> {
            assertEquals(CloseStatus.NORMAL, c);
            calls.incrementAndGet();
        });
        client.getOnClientDisconnected().invoke(CloseStatus.NORMAL);
    }
}