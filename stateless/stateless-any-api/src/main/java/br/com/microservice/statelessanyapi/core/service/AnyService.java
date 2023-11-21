package br.com.microservice.statelessanyapi.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.microservice.statelessanyapi.core.dto.AnyResponse;
import br.com.microservice.statelessanyapi.core.dto.AuthUserResponse;

@Service
public class AnyService {
	
	@Autowired
	private JwtService jwtService;

	public AnyResponse getData(String accessToken) {
		jwtService.validateToken(accessToken);
		AuthUserResponse authUser = jwtService.getAuthenticatedUser(accessToken);
		HttpStatus status = HttpStatus.OK;
		
		return new AnyResponse(status.name(), status.value(), authUser);
	}
}
