package com.bteam.fantasychess_server.config;

import com.bteam.fantasychess_server.handler.TextWebSocketHandlerExt;
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
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    final
    WebSocketService service;

    public WebSocketConfig(@Autowired WebSocketService service) {
        this.service = service;
    }

    /**
     * Provides WebSocket Servelet and specifies it's parameters
     *
     * @return {@link ServletServerContainerFactoryBean}
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    /**
     * Registers all Websocket handler and maps these to paths, that are accessible by the client,
     * although we only need one.
     *
     * @param registry {@link WebSocketHandlerRegistry}
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(createHandler(), "/ws")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setHandshakeHandler(new DefaultHandshakeHandler());
    }

    @Bean
    public WebSocketHandler createHandler() {
        return new TextWebSocketHandlerExt(service);
    }
}