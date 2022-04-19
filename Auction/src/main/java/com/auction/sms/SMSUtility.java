package com.auction.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.auction.global.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SMSUtility {
	
	public static String sendSMS(String message, String mobileNumber, String templateId) {
		HttpURLConnection con = null;
		try {
			Map<String, String> parameters = new LinkedHashMap<>();
			parameters.put("api_id", "APIBSH2Lzet82773");
			parameters.put("api_password", "Balaji_2020");
			parameters.put("sms_type", "Transactional");
			parameters.put("sms_encoding", "text");
			parameters.put("sender", "PRFCES");
			parameters.put("number", mobileNumber);
			parameters.put("message", message.replaceAll(" ", "%20"));
			parameters.put("template_id", templateId);

			StringBuilder sendSmsData = new StringBuilder("https://bulksmsplans.com/api/send_sms/?");
			sendSmsData.append(ParameterStringBuilder.getParamsString(parameters));
			URL url = new URL(sendSmsData.toString());
			
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			int status = con.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = new HashMap<String, Object>();
				map = mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>(){});
				if(null!=map) {
					return (String) map.get("code").toString();
				}
			}

		} catch (Exception ex) {
			 throw new ResourceNotFoundException("Phone number is not verified");
		} finally {
			if (con != null) {
				try {
					con.disconnect();
				} catch (Exception ex) {
					 throw new ResourceNotFoundException("Exception while disconnecting");
				}
			}
		}
		return null;
	}
	
	public static class ParameterStringBuilder {
	    public static String getParamsString(Map<String, String> params) 
	      throws UnsupportedEncodingException{
	        StringBuilder result = new StringBuilder();
	 
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	          result.append(entry.getKey());
	          result.append("=");
	          result.append(entry.getValue());
	          result.append("&");
	        }
	 
	        String resultString = result.toString();
	        return resultString.length() > 0
	          ? resultString.substring(0, resultString.length() - 1)
	          : resultString;
	    }
	}

}
