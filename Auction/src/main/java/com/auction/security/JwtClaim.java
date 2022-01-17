package com.auction.security;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class JwtClaim {
	
	private Long userId;
	private Integer organizationId;
	private List<String> authorities;
	private String email;
	
	@SuppressWarnings("unchecked")
	public static JwtClaim mapToJwtClaim(Map<String, Object> map) {
		JwtClaim claim = new JwtClaim();
		claim.setEmail(map.get("email").toString());
		claim.setOrganizationId(Integer.parseInt(map.get("organizationId").toString()));
		claim.setUserId(Long.parseLong(map.get("userId").toString()));
		Object roles = map.get("authorities");
		if(roles instanceof List) {
		  claim.setAuthorities((List<String>) roles);
		}
		return claim;
	}

}
