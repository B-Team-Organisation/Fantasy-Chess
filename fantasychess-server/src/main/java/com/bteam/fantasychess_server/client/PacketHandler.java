package com.bteam.fantasychess_server.client;

import com.bteam.common.dto.Packet;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PacketHandler {
    public void handle(Client client,String id, String packet) throws JsonProcessingException;
    public String getPacketPattern();
}
