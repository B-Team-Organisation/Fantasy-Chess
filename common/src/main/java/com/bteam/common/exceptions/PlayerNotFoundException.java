package com.bteam.common.exceptions;

public class PlayerNotFoundException extends Exception {
        public PlayerNotFoundException(String message){
            super("Player not found: "+message+")");
        }
}
