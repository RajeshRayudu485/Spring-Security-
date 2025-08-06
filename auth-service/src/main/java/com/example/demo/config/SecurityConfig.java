package com.example.demo.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests(
				auth -> auth.requestMatchers("/auth/**").permitAll()
						.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	@LoadBalanced // This enables load balancing and 'lb://' scheme support
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}
}
