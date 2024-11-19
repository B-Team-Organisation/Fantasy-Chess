package com.bteam.fantasychess_client.networking;

@FunctionalInterface
public interface PacketHandler {
    void handle(Packet packet);
}
