package com.bteam.fantasychess_server.data;

import com.bteam.fantasychess_server.client.Client;
import models.GridModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    // private Lobby lobby;
    private final UUID id;
    private final List<Client> clients;
    private GridModel grid;

    public Game(/*Lobby lobby, */ Client host) {
        clients = new ArrayList<>();
        id = UUID.randomUUID();
        addClient(host);
    }

    public void addClient(Client client) {
        //lobby.addPlayer(client.getPlayer())
        clients.add(client);
    }

    public UUID getId() {
        return id;
    }

    public List<Client> getClients() {
        return clients;
    }
}
