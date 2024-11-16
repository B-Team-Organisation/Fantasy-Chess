package com.bteam.common.exceptions;

import com.bteam.common.models.Vector2D;

public class NotAStartPositionException extends Exception{
    public NotAStartPositionException(Vector2D position){
        super("The given position isn't a valid starting position (" + position.toString() + ")");
    }
}
