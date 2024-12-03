package com.bteam.fantasychess_server.websockets;

import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSocketServiceTest {

    private static final String TEST_ID = "TEST_ID";
    private static final TestPayload TEST_PAYLOAD = new TestPayload("TEST_PAYLOAD");
    private static String testPayloadString;

    @Mock
    private WebSocketSession mockSession;

    @Mock
    private TextMessage mockTextMessage;

    @Mock
    private Client mockClient;

    @InjectMocks
    private WebSocketService webSocketService;

    @Mock
    private Player player = new Player("", "", new ArrayList<>());

    @BeforeAll
    static void setUp() throws JsonProcessingException {
        testPayloadString = new ObjectMapper().writeValueAsString(testPayloadString);
    }

    @Test
    void testInitialization() {
        webSocketService = new WebSocketService(null, null);

        assertNotNull(webSocketService.getClients());
        assertNotNull(webSocketService.getMapper());
    }

    @Test
    void testClientRegistration() {
        when(mockSession.getId()).thenReturn(TEST_ID);

        webSocketService.registerSession(mockSession, player);

        assertEquals(1, webSocketService.getClients().size());
        assertTrue(webSocketService.getClients().containsKey(TEST_ID));
    }


    @Test
    void testClientUnregistration() {
        when(mockSession.getId()).thenReturn(TEST_ID);

        webSocketService.registerSession(mockSession, player);

        assertEquals(1, webSocketService.getClients().size());
        assertEquals(TEST_ID, Objects.requireNonNull(webSocketService.getClients().get(TEST_ID)).getId());

        webSocketService.removeSession(TEST_ID, CloseStatus.NO_STATUS_CODE);

        assertEquals(0, webSocketService.getClients().size());
    }

    @Test
    void testSendMessage() throws IOException {
        when(mockSession.getId()).thenReturn(TEST_ID);

        webSocketService.registerSession(mockSession, player);
        // Todo
        //webSocketService.sendToClient(TEST_ID, TEST_PACKET);

        verify(mockSession, times(1)).sendMessage(any());
    }

    @Test
    void testMessageHandle() {
        when(mockSession.getId()).thenReturn(TEST_ID);
        when(mockTextMessage.getPayload()).thenReturn(testPayloadString);

        webSocketService.registerSession(mockSession, player);

        Objects.requireNonNull(webSocketService.getClients().get(TEST_ID))
            .getOnMessageReceivedEvent()
            .addListener(payload -> assertEquals(testPayloadString, payload));

        webSocketService.handleTextMessage(mockSession, mockTextMessage);
    }
}
