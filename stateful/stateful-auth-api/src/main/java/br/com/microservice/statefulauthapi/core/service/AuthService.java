package br.com.microservice.statefulauthapi.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.microservice.statefulauthapi.core.dto.AuthRequest;
import br.com.microservice.statefulauthapi.core.dto.AuthUserResponse;
import br.com.microservice.statefulauthapi.core.dto.RegisterRequest;
import br.com.microservice.statefulauthapi.core.dto.TokenDTO;
import br.com.microservice.statefulauthapi.core.dto.TokenData;
import br.com.microservice.statefulauthapi.core.dto.UserDTO;
import br.com.microservice.statefulauthapi.core.model.User;
import br.com.microservice.statefulauthapi.core.repository.UserRepository;
import br.com.microservice.statefulauthapi.infra.exception.AuthenticationException;
import br.com.microservice.statefulauthapi.infra.exception.UserNotFoundException;
import br.com.microservice.statefulauthapi.infra.exception.ValidationException;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenService tokenService;

	public TokenDTO login(AuthRequest request) {
		User user = findByUsername(request.username());
		validatePassword(request.password(), user.getPassword());
		String accessToken = tokenService.createToken(user.getUsername());

		return new TokenDTO(accessToken);
	}

	public UserDTO register(RegisterRequest registerRequest) {
		validateUserAlreadyRegistered(userRepository.findByUsername(registerRequest.username()));
		User newUser = User.of(registerRequest);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

		User savedUser = userRepository.save(newUser);
		return new UserDTO(savedUser.getId(), savedUser.getUsername());
	}

	public AuthUserResponse getAuthenticatedUser(String accessToken) {
		TokenData tokenData = tokenService.getTokenData(accessToken);
		User user = findByUsername(tokenData.username());

		return new AuthUserResponse(user.getId(), user.getUsername());
	}

	public Map<String, Object> logout(String accessToken) {
		tokenService.deleteRedisToken(accessToken);
		HttpStatus status = HttpStatus.OK;
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("status", status.name());
		response.put("code", status.value());
		
		return response;
	}

	public TokenDTO validateToken(String accessToken) {
		validateExistingToken(accessToken);
		boolean isValid = tokenService.validateAccessToken(accessToken);

		if (isValid) {
			return new TokenDTO(accessToken);
		}

		throw new AuthenticationException("Invalid token");
	}

	private void validateUserAlreadyRegistered(Optional<User> user) {
		if (user.isPresent()) {
			throw new ValidationException("An error occurred when trying to create the user");
		}
	}

	private User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
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
}
