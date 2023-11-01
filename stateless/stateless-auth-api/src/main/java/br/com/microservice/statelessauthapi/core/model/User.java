package br.com.microservice.statelessauthapi.core.model;

import br.com.microservice.statelessauthapi.core.dto.RegisterRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "public")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;

	public User() {
	}

	public User(Integer id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public static User of(RegisterRequest request) {
		User user = new User();
		user.setUsername(request.username());
		user.setPassword(request.password());
		
		return user;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
