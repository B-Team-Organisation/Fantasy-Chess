package com.bteam.common.exceptions;

public class GameNotRunningException extends RuntimeException {
    public GameNotRunningException(String id) {
        super("The game with the id " + id + " is not running.");
    }
}
