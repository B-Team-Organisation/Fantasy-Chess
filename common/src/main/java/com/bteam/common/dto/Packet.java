package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

/**
 * Packet data structure for moving data between Client and Server
 *
 * @author Marc
 */
public class Packet {
    JsonDTO data;
    String id;

    public Packet() {
        data = null;
        id = "";
    }

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
        return new JsonWriter().writeKeyValue("id", id)
            .and().writeKeyValue("data", data).toString();
    }
}
