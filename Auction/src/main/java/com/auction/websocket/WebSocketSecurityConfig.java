package com.auction.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig  extends
          AbstractSecurityWebSocketMessageBrokerConfigurer {
   @Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages.nullDestMatcher().authenticated().simpSubscribeDestMatchers("/queue/errors","/ws").permitAll()
				.simpDestMatchers("/eauction/**").authenticated()
				.simpSubscribeDestMatchers("/topic/**", "/queue/**").authenticated();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
  }