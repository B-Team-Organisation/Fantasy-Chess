package com.bteam.fantasychess_client.networking;

/**
 * A Simple functional Interface for the Packet Handlers
 *
 * @author Marc
 */

@FunctionalInterface
public interface PacketHandler {
    void handle(String packet);
}
