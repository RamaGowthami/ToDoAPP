package com.example.todoapp.config;

import com.example.todoapp.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Value("${app.security.require-jwt:false}")
	private boolean requireJwt;

	@Value("${app.security.username:admin}")
	private String defaultUsername;

	@Value("${app.security.password:admin123}")
	private String defaultPassword;

	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		return new InMemoryUserDetailsManager(
			User.withUsername(defaultUsername)
					.password(passwordEncoder.encode(defaultPassword))
					.roles("USER")
					.build()
		);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
		http.csrf(csrf -> csrf.disable());

		if (!requireJwt) {
			http.authorizeHttpRequests(auth -> auth
					.requestMatchers("/auth/**", "/login", "/", "/index", "/home", "/tasks/**", "/api/**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
					.anyRequest().permitAll()
			)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(Customizer.withDefaults());
			return http.build();
		}

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login", "/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/", "/index", "/home", "/tasks/**").permitAll()
				.requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
				.anyRequest().authenticated()
		)
		.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider(userDetailsService(passwordEncoder()), passwordEncoder()))
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}