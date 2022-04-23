package com.auction.security;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auction.user.IUserLoginService;
import com.auction.user.UserLogin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserServiceDetailImpl userServiceDetailImpl;
	
	@Autowired 
	private IUserLoginService userLoginService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader("Authorization");
        String unqiueTokenHeader = req.getHeader("UniqueLoginToken");
//		if (header == null || !header.startsWith("Bearer") || unqiueTokenHeader == null) {
//			chain.doFilter(req, res);
//			return;
//		}
        
    	if (header == null || !header.startsWith("Bearer")) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;
		try {
			authentication = getAuthentication(req, unqiueTokenHeader);
		} catch (SignatureException ex) {
			System.out.println("Invalid JWT Signature");
		} catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
			req.setAttribute("expired", ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported JWT exception");
		} catch (IllegalArgumentException ex) {
			System.out.println("Jwt claims string is empty");
		} catch (java.security.SignatureException e) {
			System.out.println("Jwt signature exception");
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String uniqueLoginToken)
			throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException,
			IllegalArgumentException, java.security.SignatureException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		String token = request.getHeader("Authorization");
		if (token != null) {
			String t = token.replace("Bearer ", "");
			JwtTokenUtil tokenUtility = new JwtTokenUtil();
			// parse the token.
			Jws<Claims> jwtClaims = tokenUtility.getAllClaimsFromToken(t);
			Claims claims = jwtClaims.getBody();
			String user = claims.get("username").toString();
			if (user != null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetailImpl userDetailImpl =   this.userServiceDetailImpl.loadUserByUsername(user);
				System.out.println(isUserRequestFromLatestLogin(userDetailImpl.getId(), uniqueLoginToken));
				return new UsernamePasswordAuthenticationToken(userDetailImpl, null, userDetailImpl.getAuthorities());
			}
		}
		return null;
	}
	
	private boolean isUserRequestFromLatestLogin(Long userId, String uniqueLoginToken) {
		  UserLogin userLogin =  this.userLoginService.findLastUnqiueTokenForUser(userId);
		  return (userLogin != null && userLogin.getLoginUniqueId().equals(uniqueLoginToken));
	}
}