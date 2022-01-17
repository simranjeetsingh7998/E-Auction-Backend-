package com.auction.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenUtil {

//	private static final String KEYFILE = "auctionjwt.jks";
//	private static final String PASSWORD = "auctionjwt";
//	private static final String ALIAS = "auctionjwt";
//	
//
//	public Jws<Claims> getAllClaimsFromToken(String token) throws MalformedJwtException,
//			ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
//
//		return Jwts.parserBuilder().setSigningKey(getSignedKey()).build().parseClaimsJws(token);
//	}
//
//	public String generateToken(UserDetails userDetails) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
//		return doGenerateToken(userDetails);
//	}
//
//	private String doGenerateToken(UserDetails userDetails) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
//		long currentTimeInMillSeconds = System.currentTimeMillis();
//		long expireTimeInMillSeconds = currentTimeInMillSeconds + (1000 * 60 * 60 * 24);
//		Date date = new Date(currentTimeInMillSeconds);
//		Map<String, Object> claimsMap = new HashMap<>();
//		claimsMap.put("username", userDetails.getUsername());
//		claimsMap.put("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray());
//		Claims claims = Jwts.claims(claimsMap);
//		return Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(expireTimeInMillSeconds))
//				.signWith(getSignedKey()).compact();
//	}
//	
//	private  Key getSignedKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
//		ResourceLoader resourceLoader = new FileSystemResourceLoader();
//		Resource resource =  resourceLoader.getResource("classpath:"+KEYFILE);
//		System.out.println(resource.getURI());
//		KeyStore keyStore = KeyStore.getInstance(resource.getFile(), PASSWORD.toCharArray());
//        return  keyStore.getKey(ALIAS, PASSWORD.toCharArray());
//	}
	
	
	private String secret = "$2y$12$eUovJt3tV2DxfQEspaPxcucmYcPYPBGGojGoiI1vt4Oic3hfm62w6";

	public Jws<Claims> getAllClaimsFromToken(String token) throws SignatureException, MalformedJwtException,
			ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	public String generateToken(UserDetails userDetails) {
		return doGenerateToken(userDetails);
	}

	private String doGenerateToken(UserDetails userDetails) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		long currentTimeInMillSeconds = System.currentTimeMillis();
		long expireTimeInMillSeconds = currentTimeInMillSeconds + (1000 * 60 * 60 * 24);
		Date date = new Date(currentTimeInMillSeconds);

		Map<String, Object> claimsMap = new HashMap<>();
		claimsMap.put("username", userDetails.getUsername());
		claimsMap.put("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray());
		Claims claims = Jwts.claims(claimsMap);
		return Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(expireTimeInMillSeconds))
				.signWith(key).compact();
	}


}
