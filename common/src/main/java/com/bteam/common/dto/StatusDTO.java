package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

/**
 * @author Marc
 */
public class StatusDTO implements JsonDTO {
    public String status;

    public StatusDTO() {
        status = "";
    }

    public StatusDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "{\"status\":\"" + status + "\"}";
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("status", status).toString();
    }
}
