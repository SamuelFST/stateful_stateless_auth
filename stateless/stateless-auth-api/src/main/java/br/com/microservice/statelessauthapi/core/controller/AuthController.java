package br.com.microservice.statelessauthapi.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservice.statelessauthapi.core.dto.AuthRequest;
import br.com.microservice.statelessauthapi.core.dto.RegisterRequest;
import br.com.microservice.statelessauthapi.core.dto.TokenDTO;
import br.com.microservice.statelessauthapi.core.dto.UserDTO;
import br.com.microservice.statelessauthapi.core.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public UserDTO register(@RequestBody RegisterRequest registerRequest) {
		return authService.register(registerRequest);
	}
	
	@PostMapping("/login")
	public TokenDTO login(@RequestBody AuthRequest authRequest) {
		return authService.login(authRequest);
	}
	
	@PostMapping("/token/validate")
	public TokenDTO validateToken(@RequestHeader String accessToken) {
		return authService.validateToken(accessToken);
	}
}
