package Exceptions;

import models.Vector2D;

public class DestinationAlreadyOccupiedException extends Exception{
    public DestinationAlreadyOccupiedException(Vector2D position){
        super("The specified destination is already occupied ("+position.toString()+")");
    }
}
