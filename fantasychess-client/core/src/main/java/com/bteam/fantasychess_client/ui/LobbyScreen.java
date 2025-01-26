package com.bteam.fantasychess_client.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.bteam.common.dto.Packet;
import com.bteam.common.dto.PlayerStatusDTO;
import com.bteam.common.exceptions.PlayerNotFoundException;
import com.bteam.common.models.Player;
import com.bteam.common.models.Player.Status;
import com.bteam.fantasychess_client.Main;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static com.bteam.common.constants.PacketConstants.PLAYER_LEAVE;
import static com.bteam.common.constants.PacketConstants.PLAYER_READY;
import static com.bteam.fantasychess_client.Main.*;

/**
 * A lobby screen for waiting after lobby creation.
 * <p/>
 * Shows players in lobby and offers leaving and toggeling ready status.
 * If both players are ready, the game should start.
 *
 * @author jacinto
 * @version 1.0
 */
public class LobbyScreen extends Dialog {

    private final HashMap<String, PlayerLabels> playerLabelMap = new HashMap<>();
    Skin skin;

    public LobbyScreen(Skin skin, String lobbyName) {
        super(lobbyName, skin);
        getTitleLabel().setAlignment(Align.center);
        getTitleTable().align(Align.center);

        setModal(true);
        setMovable(false);
        setResizable(false);

        this.skin = skin;

        for (Player player : getPlayerList()) {
            addPlayerInfoRow(player.getUsername(), player.getStatus());
        }

        getLobbyService().onPlayerJoined.addListener(
            player -> Gdx.app.postRunnable(() -> addPlayerInfoRow(player.getUsername(), player.getStatus()))
        );
        getLobbyService().onPlayerReadyChanged.addListener(
            player -> Gdx.app.postRunnable(() -> setPlayerStatus(player.getUsername(), player.getStatus()))
        );
        getWebSocketService().addPacketHandler(PLAYER_LEAVE, this::onPlayerLeave);

        button("< LEAVE", "leave");
        button("READY", "ready");

    }

    @Override
    public void result(Object obj) {
        if (obj.equals("leave")) {
            Gdx.app.postRunnable(this::sendAbandonPacket);
            Main.getScreenManager().navigateTo(Screens.MainMenu);
        } else if (obj.equals("ready")) {
            Status status = Status.READY;
            try {
                togglePlayerStatus(getUserName());
                status = playerLabelMap.get(getUserName()).getStatus();
            } catch (PlayerNotFoundException e) {
                Main.getLogger().log(Level.SEVERE, e.getMessage());
            }
            sendReadyStatus(status);
            cancel();
        }
    }

    private List<Player> getPlayerList() {
        return getLobbyService().getCurrentLobby().getPlayers();
    }

    private String getUserName() throws PlayerNotFoundException {
        String userID = getWebSocketService().getUserid();
        return getNameFromID(userID);
    }

    private String getNameFromID(String id) throws PlayerNotFoundException {
        List<Player> players = getPlayerList();
        return players.stream()
            .filter(player -> player.getPlayerId().equals(id))
            .findFirst()
            .map(Player::getUsername)
            .orElseThrow(() -> new PlayerNotFoundException("ID: " + id));
    }

    public void removePlayer(String playerName) {

        PlayerLabels playerLabels = playerLabelMap.get(playerName);

        if (playerLabels == null) {
            Main.getLogger().log(
                Level.SEVERE, "Cannot remove player " + playerName
            );
            return;
        }

        getContentTable().removeActor(playerLabels.getNameLabel());
        getContentTable().removeActor(playerLabels.getStatusLabel());

        playerLabelMap.remove(playerName);

        this.pack(); // resize
    }

    public void setPlayerStatus(String playerName, Status status) {
        playerLabelMap.get(playerName).updateStatus(status);
    }

    public void togglePlayerStatus(String playerName) {
        playerLabelMap.get(playerName).toggleStatus();
    }

    /**
     * Add a new row for a player, including name and status.
     *
     * @param playerName   The name of the player.
     * @param playerStatus The {@link Status} of the player.
     */
    private void addPlayerInfoRow(String playerName, Status playerStatus) {
        Label nameLabel = new Label(playerName, skin);
        Label statusLabel = new Label(playerStatus.toString(), skin);

        getContentTable().add(nameLabel)
            .expandX().fillX().padLeft(10).align(Align.right).padRight(10);
        getContentTable().right();
        getContentTable().add(statusLabel).padRight(10);

        getContentTable().row();

        playerLabelMap.put(playerName, new PlayerLabels(nameLabel, statusLabel, playerStatus));

        this.pack(); // resize
    }

    private void sendReadyStatus(Status status) {
        String clientId = Main.getWebSocketService().getUserid();
        Gdx.app.postRunnable(() -> {
            Packet packet = new Packet(
                status.equals(Status.READY) ? PlayerStatusDTO.ready(clientId) : PlayerStatusDTO.notReady(clientId),
                PLAYER_READY
            );
            getWebSocketService().send(packet);
        });
    }

    private void sendAbandonPacket() {
        var dto = PlayerStatusDTO.abandoned(getWebSocketService().getUserid());
        var packet = new Packet(dto, PLAYER_LEAVE);
        getWebSocketService().send(packet);
    }

    private void onPlayerLeave(String packet) {
        try {
            JsonReader reader = new JsonReader();
            JsonValue data = reader.parse(packet).get("data");
            String playerId = data.getString("playerId");
            var name = getNameFromID(playerId);
            removePlayer(name);
        } catch (PlayerNotFoundException e) {
            getLogger().log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Player Name and Status
     */
    private class PlayerLabels {
        private final Label nameLabel;
        private final Label statusLabel;
        private Status status;

        public PlayerLabels(Label nameLabel, Label statusLabel, Status status) {
            this.nameLabel = nameLabel;
            this.statusLabel = statusLabel;
            this.status = status;
        }

        public void updateStatus(Status status) {
            statusLabel.setText(status.toString());
            this.status = status;
        }

        public void toggleStatus() {
            statusLabel.setText(status.toggle().toString());
            status = status.toggle();
        }

        public Label getStatusLabel() {
            return this.statusLabel;
        }

        public Label getNameLabel() {
            return this.nameLabel;
        }

        public Status getStatus() {
            return this.status;
        }

        @Override
        public String toString() {
            return "[nameLabel=" + nameLabel + "; statusLabel=" + statusLabel
                + "; status=" + status + "]";
        }
    }

}
