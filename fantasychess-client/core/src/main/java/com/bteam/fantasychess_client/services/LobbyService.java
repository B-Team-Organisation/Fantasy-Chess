package com.bteam.fantasychess_client.services;

import com.badlogic.gdx.utils.JsonReader;
import com.bteam.common.models.LobbyModel;
import com.bteam.common.models.Player;
import com.bteam.common.utils.Event;

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
    public final Event<Player> onPlayerJoined = new Event<>();
    public final Event<Player> onPlayerReadyChanged = new Event<>();
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

    public void addPlayer(Player player) {
        if (currentLobby == null) return;
        if (currentLobby.getPlayers().stream()
            .anyMatch(player1 -> player1.getPlayerId().equals(player.getPlayerId()))) {
            currentLobby.removePlayer(player);
        }
        currentLobby.addPlayer(player);
    }

    public String onLobbyClosed(String packet) {
        if (currentLobby == null) return "";
        var receivedLobby = new JsonReader().parse(packet).get("data");
        var lobbyId = receivedLobby.get("lobbyId").asString();
        var reason = receivedLobby.get("reason").asString();

        getLogger().log(Level.SEVERE, reason);

        if (!Objects.equals(currentLobby.getLobbyId(), lobbyId)) return reason;

        getGameStateService().resetGame();
        getLobbyService().setCurrentLobby(null);
        return reason;
    }
}
