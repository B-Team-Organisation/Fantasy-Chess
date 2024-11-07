package Exceptions;


import java.util.Arrays;

public class WrongStartTilesException extends Exception{
    public WrongStartTilesException(int[] startTilesRows){
        super("The given position isn't a valid starting position (" + Arrays.toString(startTilesRows) + ")");
    }
}
