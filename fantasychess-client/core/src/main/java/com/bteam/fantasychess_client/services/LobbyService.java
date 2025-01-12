package com.bteam.fantasychess_client.services;

import com.badlogic.gdx.utils.JsonReader;
import com.bteam.common.models.LobbyModel;

import java.util.Objects;
import java.util.logging.Level;

import static com.bteam.fantasychess_client.Main.*;

/**
 * Service callable to manage the current lobby
 * the client is part of.
 *
 * @author Marc
 */
public class LobbyService {
    private static LobbyService instance;

    private LobbyModel currentLobby;

    /**
     * @return the current @link{LobbyModel} the user has joined,
     * or null if he is yet to join a lobby
     */
    public LobbyModel getCurrentLobby() {
        return currentLobby;
    }

    public void setCurrentLobby(LobbyModel currentLobby) {
        this.currentLobby = currentLobby;
    }

    public String onLobbyClosed(String packet) {
        if (currentLobby == null) return "";
        var receivedLobby = new JsonReader().parse(packet).get("data");
        var lobbyId = receivedLobby.get("lobbyId").asString();
        var reason = receivedLobby.get("reason").asString();

        // TODO: Change to use notification system
        getLogger().log(Level.SEVERE, reason);

        if (!Objects.equals(currentLobby.getLobbyId(), lobbyId)) return reason;

        getGameStateService().resetGame();
        getLobbyService().setCurrentLobby(null);
        return reason;
    }
}
