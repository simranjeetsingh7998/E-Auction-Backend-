package com.auction.security;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {

	private static final String KEYFILE = "auctionjwt.jks";
	private static final String PASSWORD = "auctionjwt";
	private static final String ALIAS = "auctionjwt";

	public Jws<Claims> getAllClaimsFromToken(String token) throws MalformedJwtException,
			ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {

		return Jwts.parserBuilder().setSigningKey(getSignedKey()).build().parseClaimsJws(token);
	}

	public String generateToken(UserDetails userDetails) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		return doGenerateToken(userDetails);
	}

	private String doGenerateToken(UserDetails userDetails) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		long currentTimeInMillSeconds = System.currentTimeMillis();
		long expireTimeInMillSeconds = currentTimeInMillSeconds + (1000 * 60 * 60 * 24);
		Date date = new Date(currentTimeInMillSeconds);
		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("username", userDetails.getUsername());
		Claims claims = Jwts.claims(claimsMap);
		return Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(expireTimeInMillSeconds))
				.signWith(getSignedKey()).compact();
	}
	
	private static Key getSignedKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance(new ClassPathResource(KEYFILE).getFile(), PASSWORD.toCharArray());
        return  keyStore.getKey(ALIAS, PASSWORD.toCharArray());
	}

}
