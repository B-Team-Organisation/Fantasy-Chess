package com.bteam.fantasychess_client.networking;

/**
 * A Simple functional Interface for the Packet Handlers
 *
 * @author Marc
 */

@FunctionalInterface
public interface PacketHandler {
    /**
     * @param packet JSON string in the form of
     *               {"id": "PACKET_PACKET", "data" : {}}
     */
    void handle(String packet);
}
