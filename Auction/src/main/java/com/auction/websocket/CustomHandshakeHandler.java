package com.auction.websocket;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
public class CustomHandshakeHandler implements HandshakeInterceptor {
	

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		System.out.println("bshdfbhd jsjbvb jj bej");
		if (request instanceof ServletServerHttpRequest req) {
			ServletServerHttpRequest servletRequest = req;
			 HttpSession session = servletRequest.getServletRequest().getSession(false);
			   if (session != null) {
			    attributes.put("HTTPSESSIONID", session.getId());
			   }
			  }
			  return true;
	}
    
}