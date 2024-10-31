package com.bteam.fantasychess_server.config;

import com.bteam.fantasychess_server.service.WebSocketService;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    final
    WebSocketService service;

    public WebSocketConfig(@Autowired WebSocketService service) {
        this.service = service;
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("registerWebSocketHandlers");
        registry.addHandler(createHandler(), "/ws")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor() {
                    @Override
                    public void afterHandshake(ServerHttpRequest request,
                                               ServerHttpResponse response, WebSocketHandler wsHandler,
                                               @Nullable Exception ex) {
                        super.afterHandshake(request, response, wsHandler, ex);

                    }

                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request,
                                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                                   Map<String, Object> attributes) throws Exception {

                        return super.beforeHandshake(request, response, wsHandler, attributes);
                    }
                })
                .setHandshakeHandler(new DefaultHandshakeHandler());
    }

    @Bean
    public WebSocketHandler createHandler() {
        System.out.println("createHandler");
        return new TextWebSocketHandlerExt(service);
    }
}