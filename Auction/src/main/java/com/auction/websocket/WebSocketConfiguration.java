package com.auction.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.auction.security.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	
	//@Autowired
	//private IUserDao userDao;
	
	@Autowired 
	private JwtTokenUtil tokenUtility;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	config.setPreservePublishOrder(true);
        config.enableSimpleBroker("/topic","/queue","/user");
        config.setApplicationDestinationPrefixes("/auction");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");//.setHandshakeHandler(new CustomHandshakeHandler());
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*")//.setHandshakeHandler(new CustomHandshakeHandler())
        .withSockJS().setInterceptors(httpSessionIdHandshakeInterceptor());
    }
    
    @Bean
    public HandshakeInterceptor[] httpSessionIdHandshakeInterceptor() {
    	CustomHandshakeHandler [] customHandshakeHandlers = {new CustomHandshakeHandler()};
     return customHandshakeHandlers;
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) { 
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                System.out.println(accessor.getDestination());
//                System.out.println(accessor.getCommand());
//                System.out.println(accessor.getUser());
                System.out.println(accessor.getDestination()+"        Session id   "+accessor.getSessionId());
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                   if(accessor.getNativeHeader(HttpHeaders.AUTHORIZATION)!=null) {
                		String token = accessor.getNativeHeader(HttpHeaders.AUTHORIZATION).get(0).substring(7);
                    	System.out.println(token);
                    	Jws<Claims> jws =  tokenUtility.getAllClaimsFromToken(token);
                    	Claims claims = jws.getBody();
                    	accessor.setUser(new UsernamePasswordAuthenticationToken(claims.get("username"), null, null));
                    	SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(claims.get("username"), null, null));
                	}
                	 //  setUserOnlineOrOffline(accessor.getUser().getName(), "Online");
                } else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
                  //  setUserOnlineOrOffline(accessor.getUser().getName(), "Online");
                } else if(StompCommand.SEND.equals(accessor.getCommand())){
                    System.out.println("Send message " );
                } else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
                    System.out.println("Exit ");
                 //   setUserOnlineOrOffline(accessor.getUser().getName(), OffsetDateTime.now().toString());
                } else {
                }
                return message;
            }
        });
    }
    
//    private void setUserOnlineOrOffline(String userName, String status) {
//    	  userDao.updateUserStatusByPhoneNumber(status, userName);
//    }
    
}