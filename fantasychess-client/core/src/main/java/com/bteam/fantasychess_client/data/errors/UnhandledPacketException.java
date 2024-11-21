package com.bteam.fantasychess_client.data.errors;

public class UnhandledPacketException extends RuntimeException {
    public UnhandledPacketException(String message) {
        super(message);
    }
}
