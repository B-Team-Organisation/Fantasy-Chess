package com.bteam.fantasychess_client.networking;

public class Packet{
    Object data;
    String id;

    public Packet(Object data, String id) {
        this.data = data;
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public <T> T getDataAs(Class<T> clazz) {
        return (T)getData();
    }
}
