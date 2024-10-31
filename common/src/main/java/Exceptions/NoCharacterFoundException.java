package Exceptions;

import models.Vector2D;

public class NoCharacterFoundException extends Exception{
    public NoCharacterFoundException(Vector2D position){
        super("No character found at the specified position ("+position.toString()+")");
    }
}
