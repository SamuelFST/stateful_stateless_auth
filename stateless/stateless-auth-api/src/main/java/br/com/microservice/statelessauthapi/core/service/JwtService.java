package br.com.microservice.statelessauthapi.core.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.microservice.statelessauthapi.core.model.User;
import br.com.microservice.statelessauthapi.infra.exception.AuthenticationException;
import br.com.microservice.statelessauthapi.infra.exception.ValidationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${app.token.secret-key}")
	private String secretKey;
	private static final int TOKEN_INDEX = 1;
	
	public String createToken(User user) {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("id", user.getId().toString());
		data.put("username", user.getUsername());
		
		return Jwts
				.builder()
				.setClaims(data)
				.setExpiration(generateExpiresAt())
				.signWith(generateSign())
				.compact();
	}
	
	public void validateToken(String token) {
		String accessToken = extractToken(token);
		
		try {
			Jwts
				.parserBuilder()
				.setSigningKey(generateSign())
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (Exception ex) {
			throw new AuthenticationException("Invalid token: " + ex.getMessage());
		}
	}
	
	private Date generateExpiresAt() {
		return Date.from(LocalDateTime.now().plusHours(24).atZone(ZoneId.systemDefault()).toInstant());				
	}
	
	private SecretKey generateSign() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	private String extractToken(String token) {
		if (token.isEmpty()) {
			throw new ValidationException("Access token not informed");
		}
		
		if (token.contains(" ")) {
			return token.split(" ")[TOKEN_INDEX];
		}
		
		return token;
	}
}
