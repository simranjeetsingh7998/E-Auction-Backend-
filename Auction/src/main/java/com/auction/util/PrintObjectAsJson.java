package com.auction.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrintObjectAsJson {
	
	public static final void printAsJson(Object object) {
		 ObjectMapper mapper = new ObjectMapper();
		 try {
			String json = mapper.writeValueAsString(object);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
