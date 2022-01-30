package com.auction.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;


@Controller
public class SocketTestController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
//	@MessageMapping("/message/{chatRoom}")
//	public void sendMessageToRoom(@DestinationVariable("chatRoom") String chatRoom, @Payload MessageVO messageVO) {
//		String mobileNumber = SecurityContextHolder.getContext().getAuthentication().getName();
//		this.simpMessagingTemplate.convertAndSend("/queue/message/"+chatRoom, message); 
//	}
	
	@MessageMapping("/message/{chatRoom}")
	public void sendMessageToRoom(@DestinationVariable("chatRoom") String chatRoom) {
		String mobileNumber = SecurityContextHolder.getContext().getAuthentication().getName();
		this.simpMessagingTemplate.convertAndSend("/queue/message/"+chatRoom, mobileNumber); 
	}
}
