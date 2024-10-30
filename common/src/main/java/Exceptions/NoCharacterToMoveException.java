package Exceptions;

public class NoCharacterToMoveException extends Exception{
    public NoCharacterToMoveException(){
        super("No character in the given tile!");
    }
}
