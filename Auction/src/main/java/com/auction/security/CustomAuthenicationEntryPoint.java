package com.auction.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenicationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if(request.getAttribute("expired") != null) {
		     ObjectMapper mapper = new ObjectMapper();
		        Map<String, String> map = new HashMap<>();
		        map.put("message", authException.getMessage());
		        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		        response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(mapper.writeValueAsString(map));
		} else {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Worng Credentials");
	}
	}

}