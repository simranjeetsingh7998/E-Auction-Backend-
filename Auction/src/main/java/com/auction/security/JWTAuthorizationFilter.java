package com.auction.security;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer")) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;
		try {
			authentication = getAuthentication(req);
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

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request)
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

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}