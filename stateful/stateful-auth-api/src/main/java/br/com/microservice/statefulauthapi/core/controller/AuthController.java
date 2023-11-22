package br.com.microservice.statefulauthapi.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservice.statefulauthapi.core.dto.AuthRequest;
import br.com.microservice.statefulauthapi.core.dto.AuthUserResponse;
import br.com.microservice.statefulauthapi.core.dto.RegisterRequest;
import br.com.microservice.statefulauthapi.core.dto.TokenDTO;
import br.com.microservice.statefulauthapi.core.dto.UserDTO;
import br.com.microservice.statefulauthapi.core.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@GetMapping("/user")
	public AuthUserResponse getAuthenticatedUser(@RequestHeader String accessToken) {
		return authService.getAuthenticatedUser(accessToken);
	}
	
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
	
	@PostMapping("/logout")
	public Map<String, Object> logout(@RequestHeader String accessToken) {
		return authService.logout(accessToken);
	}
}
