package app.netbooks.backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
    @Override
    public void registerStompEndpoints(
        @SuppressWarnings("null") StompEndpointRegistry registry
    ) {
        registry
            .addEndpoint("/websocket")
            .setAllowedOrigins("*");
    };

    @Override
    public void configureMessageBroker(
        @SuppressWarnings("null") MessageBrokerRegistry registry
    ){
        registry.enableSimpleBroker("/channel/events/rooms");
        registry.setApplicationDestinationPrefixes("/channel/triggers/rooms");
    };
}
