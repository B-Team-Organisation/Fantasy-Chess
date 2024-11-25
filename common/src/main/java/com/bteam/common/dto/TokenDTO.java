package com.bteam.common.dto;
/**
 * Data Transfer Object for tokens
 *
 * @author Marc
 */
public class TokenDTO implements JsonDTO{
    private String token;
    private long expires;

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
        return "{\"token\":\""+token+"\",\"expires\":"+expires+"}";
    }
}
