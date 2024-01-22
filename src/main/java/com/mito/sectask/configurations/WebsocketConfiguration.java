package com.mito.sectask.configurations;

import com.google.gson.Gson;
import com.mito.sectask.annotations.sender.SenderResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.GsonMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfiguration
    implements WebSocketMessageBrokerConfigurer {

    private final Gson gson;
    private final SenderResolver senderResolver;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(
        StompEndpointRegistry stompEndpointRegistry
    ) {
        stompEndpointRegistry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

    @Override
    public boolean configureMessageConverters(
        List<MessageConverter> messageConverters
    ) {
        GsonMessageConverter messageConverter = new GsonMessageConverter(gson);
        messageConverters.add(messageConverter);
        return false;
    }

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> resolvers
    ) {
        WebSocketMessageBrokerConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(senderResolver);
    }
}
