package com.bteam.fantasychess_client.data.errors;
/**
 * @author Marc
 */
public class UnhandledPacketException extends RuntimeException {
    public UnhandledPacketException(String message) {
        super(message);
    }
}
