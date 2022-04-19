package com.auction.staticdata.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.api.response.ApiResponse;

@RestController
public class StaticController {
	
	@GetMapping("/static/current/date/time")
	public ResponseEntity<ApiResponse> getCurrentDateTime(){
		return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK.value(), "Server Date Time", 
				new Date().getTime(), null), HttpStatus.OK);
	}

}
