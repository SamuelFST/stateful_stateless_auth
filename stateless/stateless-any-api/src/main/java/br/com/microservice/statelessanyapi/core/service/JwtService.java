package br.com.microservice.statelessanyapi.core.service;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.microservice.statelessanyapi.core.dto.AuthUserResponse;
import br.com.microservice.statelessanyapi.infra.exception.AuthenticationException;
import br.com.microservice.statelessanyapi.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${app.token.secret-key}")
	private String secretKey;
	private static final int TOKEN_INDEX = 1;

	public AuthUserResponse getAuthenticatedUser(String token) {
		Claims tokenClaims = getClaims(token);

		Integer userId = Integer.valueOf((String) tokenClaims.get("id"));
		String username = (String) tokenClaims.get("username");

		return new AuthUserResponse(userId, username);
	}

	public void validateToken(String token) {
		getClaims(token);
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

	private Claims getClaims(String token) {
		String accessToken = extractToken(token);

		try {
			return Jwts
					.parserBuilder()
					.setSigningKey(generateSign())
					.build()
					.parseClaimsJws(accessToken)
					.getBody();
		} catch (Exception ex) {
			throw new AuthenticationException("Invalid token: " + ex.getMessage());
		}
	}

	private SecretKey generateSign() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
}
