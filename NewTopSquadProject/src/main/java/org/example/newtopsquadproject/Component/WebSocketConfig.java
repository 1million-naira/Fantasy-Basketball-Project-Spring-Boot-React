package org.example.newtopsquadproject.Component;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.newtopsquadproject.Security.CustomUserDetailsService;
import org.example.newtopsquadproject.Security.JwtDecoder;
import org.example.newtopsquadproject.Security.JwtToPrincipalConverter;
import org.example.newtopsquadproject.Security.UserPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(3)
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtDecoder jwtDecoder;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    private final CustomUserDetailsService customUserDetailsService;

    public WebSocketConfig(JwtDecoder jwtDecoder, JwtToPrincipalConverter jwtToPrincipalConverter, CustomUserDetailsService customUserDetailsService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtToPrincipalConverter = jwtToPrincipalConverter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/api/chat/", "/api/notifications/", "/api/general", "/api/admin");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:3000")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                log.info("Headers: {}", accessor);
                assert accessor != null;
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    assert authorizationHeader != null;
                    String token = authorizationHeader.substring(7);
                    UserPrincipal userPrincipal = jwtToPrincipalConverter.convert(jwtDecoder.decode(token));
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userPrincipal.getEmail());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    SocketUser socketUser = new SocketUser(userPrincipal.getUserId());
                    //This is to access the ID of the currently authenticated user
                    accessor.setUser(socketUser);

                    //The idea here was to get the token from the initial HTTP request to connect to websocket
                    //Then decode this token and set a principal for the websocket session
                    //Because HTTP and websocket connection are different
                }
                return message;
            }
        });
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MediaType.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }









    //    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setHandshakeHandler(new DefaultHandshakeHandler(){
//                    //Interceptor to obtain sessionId before client and server handshake through websocket
//                    public boolean beforeHandshake(
//                            ServerHttpRequest request,
//                            ServerHttpResponse response,
//                            WebSocketHandler wsHandler,
//                            Map<Object, Object> attributes) throws Exception{
//
//                        if(request instanceof ServletServerHttpRequest serverHttpRequest){ //Setting serverHttpRequest if true
//                            HttpSession session = serverHttpRequest.getServletRequest().getSession();
//                            attributes.put("sessionId", session.getId());
//                        }
//
//                        return true;
//
//                    }
//                }
//                )
//                .setAllowedOriginPatterns("http://localhost:3000")
//                .withSockJS();
//    }
}
