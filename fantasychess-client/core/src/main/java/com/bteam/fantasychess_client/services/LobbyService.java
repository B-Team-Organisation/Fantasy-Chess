package com.bteam.fantasychess_client.services;

import com.bteam.common.models.LobbyModel;

public class LobbyService {
    private static LobbyService instance;

    private LobbyModel currentLobby;

    public LobbyService() {
        instance = this;
    }

    public static LobbyService getInstance() {
        if (instance == null) {
            instance = new LobbyService();
        }
        return instance;
    }

    public LobbyModel getCurrentLobby() {
        return currentLobby;
    }

    public void setCurrentLobby(LobbyModel currentLobby) {
        this.currentLobby = currentLobby;
    }
}
