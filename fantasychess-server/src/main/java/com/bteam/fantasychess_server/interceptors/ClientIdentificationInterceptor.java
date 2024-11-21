package com.bteam.fantasychess_server.interceptors;

import com.bteam.fantasychess_server.service.TokenService;
import com.bteam.fantasychess_server.utils.UriUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * Used to Identify a Player via a token, once they try to establish a WebSocket connection
 * @author Marc
 */
public class ClientIdentificationInterceptor extends HttpSessionHandshakeInterceptor {
    TokenService tokenService;

    public ClientIdentificationInterceptor(TokenService tokenService) {
        super();
        this.tokenService = tokenService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        var queryMap = UriUtils.getQueryParameters(request.getURI());
        var token = queryMap.get("token");
        return tokenService.checkToken(token) && super.beforeHandshake(request, response, wsHandler, attributes);
    }
}

