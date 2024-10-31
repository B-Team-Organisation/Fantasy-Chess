package com.bteam.fantasychess_server.websockets;

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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

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

    @BeforeAll
    static void setUp() throws JsonProcessingException {
        testPayloadString = new ObjectMapper().writeValueAsString(testPayloadString);
    }

    @Test
    void testInitialization() {
        webSocketService = new WebSocketService();

        assertNotNull(webSocketService.getClients());
        assertNotNull(webSocketService.getMapper());
    }

    @Test
    void testClientRegistration() {
        when(mockSession.getId()).thenReturn(TEST_ID);

        webSocketService.registerSession(mockSession);

        assertEquals(1, webSocketService.getClients().size());
        assertTrue(webSocketService.getClients().containsKey(TEST_ID));
    }

    @Test
    void testSendMessage() {
        webSocketService.getClients().put(TEST_ID, mockClient);
        webSocketService.sendToClient(TEST_ID, TEST_PAYLOAD);

        verify(webSocketService.getClients().get(TEST_ID), times(1)).sendMessage(TEST_PAYLOAD);
    }

    @Test
    void testMessageHandle() throws Exception {
        when(mockSession.getId()).thenReturn(TEST_ID);
        when(mockTextMessage.getPayload()).thenReturn(testPayloadString);

        webSocketService.registerSession(mockSession);

        webSocketService.getClients().get(TEST_ID)
                .getOnMessageRecievedEvent()
                .AddListener(payload -> assertEquals(testPayloadString, payload));

        webSocketService.handleTextMessage(mockSession, mockTextMessage);
    }
}
