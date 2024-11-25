package com.bteam.common.dto;

public class TokenDTO implements JsonDTO{
    private String token;
    private long expires;

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
