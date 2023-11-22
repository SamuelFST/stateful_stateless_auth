package br.com.microservice.statefulauthapi.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.microservice.statefulauthapi.core.dto.TokenData;
import br.com.microservice.statefulauthapi.infra.exception.AuthenticationException;
import br.com.microservice.statefulauthapi.infra.exception.ValidationException;

@Service
public class TokenService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private ObjectMapper objectMapper;

	private static final Integer TOKEN_INDEX = 1;
	private static final Long ONE_DAY_IN_SECONDS = 86400L;

	public String createToken(String username) {
		String accessToken = UUID.randomUUID().toString();
		TokenData data = new TokenData(username);
		String jsonData = getJsonData(data);

		redisTemplate.opsForValue().set(accessToken, jsonData);
		redisTemplate.expireAt(accessToken, Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));

		return accessToken;
	}

	public TokenData getTokenData(String token) {
		String accessToken = extractToken(token);
		String jsonString = getRedisTokenValue(accessToken);

		try {
			return objectMapper.readValue(jsonString, TokenData.class);
		} catch (Exception ex) {
			throw new AuthenticationException(
					"Error when trying to extract the authenticated user access token: " + ex.getMessage());
		}
	}

	public boolean validateAccessToken(String token) {
		String accessToken = extractToken(token);
		String data = getRedisTokenValue(accessToken);

		return !isEmpty(data);
	}

	public void deleteRedisToken(String token) {
		String accessToken = extractToken(token);
		redisTemplate.delete(accessToken);
	}

	private String getRedisTokenValue(String token) {
		return redisTemplate.opsForValue().get(token);
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

	private String getJsonData(Object payload) {
		try {
			return objectMapper.writeValueAsString(payload);
		} catch (Exception ex) {
			return null;
		}
	}
}
