package com.bteam.common.dto;

/**
 * Packet data structure for moving data between Client and Server
 * @author Marc
 */
public class Packet {
    JsonDTO data;
    String id;

    public Packet(JsonDTO data, String id) {
        this.data = data;
        this.id = id;
    }

    public JsonDTO getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        var dataString = data == null ? "null" : data.toString();
        return "{\"id\":\"" + id + "\",\"data\":" + dataString + "}";
    }
}
