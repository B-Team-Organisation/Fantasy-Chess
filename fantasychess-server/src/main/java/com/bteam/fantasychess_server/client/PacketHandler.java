package com.bteam.fantasychess_server.client;

import com.bteam.common.dto.Packet;

public interface PacketHandler {
    public void handle(Client client, Packet packet);
    public String getPacketPattern();
}
