package br.com.microservice.statelessauthapi.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.microservice.statelessauthapi.core.dto.AuthRequest;
import br.com.microservice.statelessauthapi.core.dto.RegisterRequest;
import br.com.microservice.statelessauthapi.core.dto.TokenDTO;
import br.com.microservice.statelessauthapi.core.dto.UserDTO;
import br.com.microservice.statelessauthapi.core.model.User;
import br.com.microservice.statelessauthapi.core.repository.UserRepository;
import br.com.microservice.statelessauthapi.infra.exception.UserNotFoundException;
import br.com.microservice.statelessauthapi.infra.exception.ValidationException;

@Service
public class AuthService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository userRepository;
	
	public UserDTO register(RegisterRequest registerRequest) {
		validateUserAlreadyRegistered(userRepository.findByUsername(registerRequest.username()));
		User newUser = User.of(registerRequest);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		
		User savedUser = userRepository.save(newUser);
		return new UserDTO(savedUser.getId(), savedUser.getUsername());
	}
	
	public TokenDTO login(AuthRequest authRequest) {
		User user = userRepository
				.findByUsername(authRequest.username())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		validatePassword(authRequest.password(), user.getPassword());
		
		return new TokenDTO(jwtService.createToken(user));
	}
	
	public TokenDTO validateToken(String accessToken) {
		validateExistingToken(accessToken);
		jwtService.validateToken(accessToken);
		return new TokenDTO(accessToken);
	}
	
	private void validatePassword(String rawPassword, String encodedPassword) {
		if (isEmpty(rawPassword)) {
			throw new ValidationException("The password must be informed");
		}
		
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new ValidationException("User and password doens't match");
		}
	}
	
	private void validateExistingToken(String accessToken) {
		if (isEmpty(accessToken)) {
			throw new ValidationException("The access token must be informed");
		}
	}
	
	private void validateUserAlreadyRegistered(Optional<User> user) {
		if (user.isPresent()) {
			throw new ValidationException("An error occurred when trying to create the user");
		}
	}
}
