package com.client.user;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.client.config.CookieTokenExtractor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

	    private final WebClient.Builder webClientBuilder;
	    private final CookieTokenExtractor cookieTokenExtractor;

	    private static final String API_GATEWAY_SERVICE_NAME = "API-GATEWAY";
	    private static final String USER_PROFILE_PATH_ON_GATEWAY = "/user/profile";
	    
	    @GetMapping("/profile/user")
	    public Mono<ResponseEntity<String>> getUserProfile(
	            @RequestHeader(value = HttpHeaders.COOKIE, required = false) String cookie) {

	        String token = cookieTokenExtractor.extractTokenFromCookie(cookie);
	        if (token == null || token.isEmpty()) {
	            log.warn(" Missing Authorization token from cookie");
	            return Mono.just(ResponseEntity.badRequest().body("Authorization token is missing"));
	        }

	        log.info(" Fetching user profile using token: {}", token);

	        return webClientBuilder.build()
	                .get()
	                .uri("lb://" + API_GATEWAY_SERVICE_NAME + USER_PROFILE_PATH_ON_GATEWAY)
	                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
	                .retrieve()
	                .toEntity(String.class)
	                .doOnSuccess(res -> log.info(" User profile fetched with status: {}", res.getStatusCode()))
	                .doOnError(e -> log.error(" Failed to fetch user profile", e))
	                .onErrorMap(e -> new RuntimeException("Error fetching user profile", e));
	    }
}
