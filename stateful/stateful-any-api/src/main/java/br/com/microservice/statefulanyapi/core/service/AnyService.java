package br.com.microservice.statefulanyapi.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.microservice.statefulanyapi.core.dto.AnyResponse;
import br.com.microservice.statefulanyapi.core.dto.AuthUserResponse;

@Service
public class AnyService {

	@Autowired
	private TokenService tokenService;
	
	public AnyResponse getAnyData(String accessToken) {
		tokenService.validateToken(accessToken);
		AuthUserResponse authUser = tokenService.getAuthenticatedUser(accessToken);
		HttpStatus status = HttpStatus.OK;
		
		return new AnyResponse(status.name(), status.value(), authUser);
	}
}
