package br.com.microservice.statefulauthapi.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException validationException) {
		ExceptionDetails details = new ExceptionDetails(HttpStatus.BAD_REQUEST.value(),
				validationException.getMessage());
		return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException authenticationException) {
		ExceptionDetails details = new ExceptionDetails(HttpStatus.UNAUTHORIZED.value(),
				authenticationException.getMessage());
		return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
		ExceptionDetails details = new ExceptionDetails(HttpStatus.NOT_FOUND.value(),
				userNotFoundException.getMessage());
		return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
	}
}
