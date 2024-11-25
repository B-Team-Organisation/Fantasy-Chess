package com.bteam.fantasychess_server.client;

import com.bteam.common.dto.Packet;

public interface PacketHandler {
    public void handle(Client client,String id, String packet);
    public String getPacketPattern();
}
