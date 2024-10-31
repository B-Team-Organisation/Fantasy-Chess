package com.bteam.fantasychess_server.config;

import com.bteam.fantasychess_server.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

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
                .setHandshakeHandler(new DefaultHandshakeHandler());
    }

    @Bean
    public WebSocketHandler createHandler() {
        System.out.println("createHandler");
        return new TextWebSocketHandlerExt(service);
    }
}