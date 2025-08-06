package com.example.demo.controller;

import com.example.demo.config.CookieTokenExtractor;
import com.example.demo.config.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthProfileController {

    private static final Logger logger = LoggerFactory.getLogger(AuthProfileController.class);

    private final WebClient.Builder webClientBuilder;
    private final JwtService jwtService;
    private final CookieTokenExtractor cookieTokenExtractor;

    private static final String API_GATEWAY_SERVICE_NAME = "API-GATEWAY";
    private static final String USER_PROFILE_PATH_ON_GATEWAY = "/user/profile";

    // Constructor injection for all dependencies
    public AuthProfileController(WebClient.Builder webClientBuilder,
                                 JwtService jwtService,
                                 CookieTokenExtractor cookieTokenExtractor) {
        this.webClientBuilder = webClientBuilder;
        this.jwtService = jwtService;
        this.cookieTokenExtractor = cookieTokenExtractor;
    }
    @GetMapping("/profile/user")
    public Mono<ResponseEntity<String>> getUserProfile(
            @RequestHeader(value = HttpHeaders.COOKIE, required = false) String cookie) {

        String token = cookieTokenExtractor.extractTokenFromCookie(cookie);
        if (token == null || token.isEmpty()) {
            logger.warn("[getUserProfile] Missing Authorization token from cookie");
            return Mono.just(ResponseEntity.badRequest().body("Authorization token is missing"));
        }

        logger.info("[getUserProfile] Fetching user profile using token: {}", token);

        return webClientBuilder.build()
                .get()
                .uri("lb://" + API_GATEWAY_SERVICE_NAME + USER_PROFILE_PATH_ON_GATEWAY)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(res -> logger.info("[getUserProfile] User profile fetched successfully with status: {}", res.getStatusCode()))
                .doOnError(e -> logger.error("[getUserProfile] Failed to fetch user profile", e))
                .onErrorMap(e -> new RuntimeException("[getUserProfile] Error fetching user profile", e));
    }

}
