package com.bteam.common.dto;

/**
 * @author Marc
 */
public class StatusDTO implements JsonDTO {
    public String status;

    public StatusDTO() {
        status = "";
    }

    public StatusDTO( String status ) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "{\"status\":\""+status+"\"}";
    }

    @Override
    public String toJson() {
        return "{\"status\":\""+status+"\"}";
    }
}
