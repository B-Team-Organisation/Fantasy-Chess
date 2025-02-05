package com.bteam.common.dto;

import com.bteam.common.utils.JsonWriter;

/**
 * Data Transfer Object for tokens
 *
 * @author Marc
 */
public class TokenDTO implements JsonDTO {
    private final String token;
    private final long expires;

    public TokenDTO() {
        token = null;
        expires = 0;
    }

    public TokenDTO(String token, long expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public long getExpires() {
        return expires;
    }

    @Override
    public String toJson() {
        return new JsonWriter().writeKeyValue("token", token)
            .and().writeKeyValue("expires", expires).toString();
    }
}
