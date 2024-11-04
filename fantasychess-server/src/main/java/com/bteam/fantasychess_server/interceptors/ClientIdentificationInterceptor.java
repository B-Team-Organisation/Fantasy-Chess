package com.bteam.fantasychess_server.interceptors;

import com.bteam.fantasychess_server.service.TokenService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClientIdentificationInterceptor extends HttpSessionHandshakeInterceptor {
    TokenService tokenService;

    public ClientIdentificationInterceptor(TokenService playerService) {
        super();
        this.tokenService = playerService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        var uri = request.getURI();
        var queryMap = new HashMap<String, String>();
        Arrays.stream(uri.getQuery().split("&"))
                .map(s -> s.split("="))
                .forEach(s -> queryMap.put(s[0], s[1]));
        var token = queryMap.get("token");

        return tokenService.invalidateToken(token) && super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
