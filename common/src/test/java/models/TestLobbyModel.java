package models;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bteam.common.models.Player;
import com.bteam.common.models.LobbyModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestLobbyModel {
    private LobbyModel lobby;
    private Player player1;
    private Player player2;
    private Player player3;  // for testing full capacity

    @BeforeEach
    public void setUp() {
        String lobbyId ="exampleLobbyId";
        List<Player> players = new ArrayList<>();  // Leere Liste, um initial keine Spieler hinzuzufügen
        player1 = new Player("Player1",  "1");
        player2 = new Player("Player2",  "2");
        player3 = new Player("Player3",  "3");  // für das Testen der vollen Kapazität

        // Erstellen des LobbyModel-Objekts mit allen benötigten Parametern
        lobby = new LobbyModel(lobbyId, players, player1, "lobby1"); // player1 ist der Host
    }

    @Test
    public void testLobbyInitialization() {
        assertNotNull(lobby.getLobbyId());
        assertEquals(1, lobby.getPlayers().size());
        assertEquals(LobbyModel.GameState.OPEN, lobby.getGameState());
        assertEquals(player1, lobby.getHost());
    }

    @Test
    public void testIsHost() {
        assertTrue(lobby.isHost(player1));
        assertFalse(lobby.isHost(player2));
    }

    @Test
    public void testAddPlayer() {
        lobby.addPlayer(player1);

        assertEquals(2, lobby.getPlayers().size());
        assertEquals(LobbyModel.GameState.FULL, lobby.getGameState());
    }

    @Test
    public void testAddPlayerToFullLobby() {
        lobby.addPlayer(player1);

        assertThrows(IllegalStateException.class, () -> lobby.addPlayer(player3));
    }

    @Test
    public void testRemovePlayer() {
        lobby.addPlayer(player1);
        lobby.removePlayer(player1);

        assertEquals(1, lobby.getPlayers().size());
        assertEquals(LobbyModel.GameState.OPEN, lobby.getGameState());
    }

    @Test
    public void setGameState() {
        lobby.addPlayer(player1);
        lobby.setGameState(LobbyModel.GameState.FULL);

        assertEquals(LobbyModel.GameState.FULL, lobby.getGameState());
    }
}
