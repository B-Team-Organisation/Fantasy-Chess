package com.bteam.common.exceptions;

import com.bteam.common.models.Vector2D;

public class DestinationInvalidException extends Exception{
    public DestinationInvalidException(Vector2D position){
        super("Move destination ("+position.toString()+") is invalid!");
    }
}
