package com.bteam.fantasychess_server.client.interceptors;

import com.bteam.common.dto.CreateLobbyDTO;
import com.bteam.common.dto.JoinLobbyDTO;
import com.bteam.common.dto.Packet;
import com.bteam.common.models.LobbyModel;
import com.bteam.common.models.Player;
import com.bteam.fantasychess_server.client.Client;
import com.bteam.fantasychess_server.service.LobbyService;
import com.bteam.fantasychess_server.service.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LobbyPacketHandlerTest {
    @InjectMocks
    private LobbyPacketHandler lobbyPacketHandler; // The class we are testing

    @Mock
    private LobbyService lobbyService; // Mocking the LobbyService

    @Mock
    private Client client; // Mocking the Client

    // Mocking ObjectMapper
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WebSocketService webSocketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        lobbyPacketHandler = new LobbyPacketHandler(lobbyService, webSocketService);
    }

    @Test
    void testHandleLobbyAll() throws JsonProcessingException {
        // Setup
        String packet = "{\"data\":\"{}\"}"; // Example packet data
        String id = "LOBBY_ALL"; // The ID we are testing

        // Mock the behavior of lobbyService
        when(lobbyService.getAllLobbies()).thenReturn(List.of()); // Return a mock list of lobbies

        // Call the method
        lobbyPacketHandler.handle(client, id, packet);

        // Verify that the sendPacket method was called with the correct packet
        verify(client, times(1)).sendPacket(any(Packet.class));
    }

    @Test
    void testHandleLobbyCreate() throws JsonProcessingException {
        // Setup
        Packet p = new Packet(new CreateLobbyDTO("Test Lobby"), "LOBBY_CREATE");
        String packet = p.toString();
        String id = "LOBBY_CREATE"; // The ID we are testing

        CreateLobbyDTO createLobbyDTO = new CreateLobbyDTO("Test Lobby");
        LobbyModel mockLobby = new LobbyModel("", new ArrayList<>(), new Player("", "", List.of()), "", 2); // Mock lobby creation

        // Mock dependencies
        when(objectMapper.readValue(anyString(), eq(CreateLobbyDTO.class))).thenReturn(createLobbyDTO);
        when(lobbyService.createNewLobby(any(), anyString(), anyInt())).thenReturn(mockLobby);

        // Call the method
        lobbyPacketHandler.handle(client, id, packet);

        // Verify that the sendPacket method was called
        verify(client, times(1)).sendPacket(any(Packet.class));
    }

    @Test
    void testHandleLobbyJoin() throws JsonProcessingException {
        // Setup
        JoinLobbyDTO joinLobbyDTO = new JoinLobbyDTO(UUID.randomUUID().toString());
        String id = "LOBBY_JOIN"; // The ID we are testing
        Packet p = new Packet(new JoinLobbyDTO(UUID.randomUUID().toString()), "LOBBY_JOIN");
        String packet = p.toString();
        System.out.println(p);

        // Mock dependencies
        when(objectMapper.readValue(anyString(), eq(JoinLobbyDTO.class))).thenReturn(joinLobbyDTO);
        when(lobbyService.joinLobby(any(UUID.class), any(UUID.class))).thenReturn(true); // Assume joining succeeds
        when(lobbyService.getLobby(any())).thenReturn(new LobbyModel("", new ArrayList<>(), new Player("", "", List.of()), "", 2));
        when(client.getPlayer()).thenReturn(new Player("", UUID.randomUUID().toString(), List.of()));

        // Call the method
        lobbyPacketHandler.handle(client, id, packet);

        // Verify that the sendPacket method was called
        verify(client, times(1)).sendPacket(any(Packet.class));
    }

    @Test
    void testHandleUnknownPacket() throws JsonProcessingException {
        // Setup
        String packet = "{\"data\":\"{}\"}";
        String id = "UNKNOWN_PACKET"; // An unknown ID

        // Call the method
        lobbyPacketHandler.handle(client, id, packet);

        // No interactions with client.sendPacket should have occurred
        verify(client, never()).sendPacket(any(Packet.class));
    }

    @Test
    void testGetPacketPattern() {
        // Verify the packet pattern is as expected
        assertEquals("LOBBY_", lobbyPacketHandler.getPacketPattern());
    }
}