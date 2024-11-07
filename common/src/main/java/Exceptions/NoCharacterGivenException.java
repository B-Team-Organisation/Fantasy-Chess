package Exceptions;

import models.Vector2D;

public class NoCharacterGivenException extends Exception{
    public NoCharacterGivenException( ){
        super("No character given to place");
    }
}
