package Exceptions;

import java.util.UUID;

public class GameNotRunningException extends RuntimeException {
    public GameNotRunningException(UUID id) {
        super(String.format("The game with the id %s is not running.", id));
    }
}
