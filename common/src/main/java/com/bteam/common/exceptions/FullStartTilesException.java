package com.bteam.common.exceptions;

public class FullStartTilesException extends Exception{
    public FullStartTilesException(){
        super("There are more Characters than tiles allowed, position is not a start tile");
    }
}
