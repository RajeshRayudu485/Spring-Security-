package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.jwt.JwtUtils;


@RestController
public class GreetingsController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;

	@GetMapping("/hello")
	public String sayHello() {
		return "HELLO" ;
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/user")
	public String userEndPoint() {
		return "HELLO  USER !" ;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public String adminEndPoint() {
		return "HELLO   ADMIN !" ;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
	    try {
	        Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                loginRequest.getUserName(),
	                loginRequest.getPassword()
	            )
	        );

	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);

	        List<String> roles = userDetails.getAuthorities().stream()
	            .map(item -> item.getAuthority())
	            .collect(Collectors.toList());

	        LoginResponse response = new LoginResponse( jwtToken,userDetails.getUsername(), roles);

	        return ResponseEntity.ok(response);

	    } catch (AuthenticationException ex) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("message", "Bad credentials");
	        map.put("status", false);
	        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED); // 401 is correct here
	    }
	}

	
}
