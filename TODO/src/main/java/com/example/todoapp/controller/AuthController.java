package com.example.todoapp.controller;

import com.example.todoapp.dto.ApiResponse;
import com.example.todoapp.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Value("${app.security.require-jwt:false}")
	private boolean requireJwt;

	public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password())
		);
		UserDetails user = (UserDetails) auth.getPrincipal();
		String token = jwtService.generateToken(user);
		return ResponseEntity.ok(ApiResponse.success("Login successful", Map.of(
				"token", token,
				"requireJwt", requireJwt
		)));
	}
}

