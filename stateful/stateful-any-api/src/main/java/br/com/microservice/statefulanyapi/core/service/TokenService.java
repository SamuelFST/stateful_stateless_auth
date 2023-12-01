package br.com.microservice.statefulanyapi.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.microservice.statefulanyapi.core.client.TokenClient;
import br.com.microservice.statefulanyapi.core.dto.AuthUserResponse;
import br.com.microservice.statefulanyapi.core.dto.TokenDTO;
import br.com.microservice.statefulanyapi.infra.exception.AuthenticationException;

@Service
public class TokenService {

	@Autowired
	private TokenClient tokenClient;

	public void validateToken(String accessToken) {
		try {
			System.out.println("Sending request for token validation: " + accessToken);
			TokenDTO response = tokenClient.validateToken(accessToken);
			System.out.println("Token is valid: " + response.accessToken());
		} catch (Exception ex) {
			throw new AuthenticationException("Authentication error: " + ex.getMessage());
		}
	}

	public AuthUserResponse getAuthenticatedUser(String accessToken) {
		try {
			System.out.println("Sending request for get auth user: " + accessToken);
			AuthUserResponse response = tokenClient.getAuthenticatedUser(accessToken);
			System.out.println("Authenticated user found: " + response.toString() + " with token: " + accessToken);

			return response;
		} catch (Exception ex) {
			throw new AuthenticationException(
					"An error ocurred when trying to get the authenticated user: " + ex.getMessage());
		}
	}
}
