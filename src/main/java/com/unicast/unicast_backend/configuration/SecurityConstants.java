package com.unicast.unicast_backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConstants {

    public final String AUTH_LOGIN_URL = "/api/public/authenticate";
    public final Integer TOKEN_TTL_MS = 10 * 24 * 3600 * 1000;

    public final String JWT_SECRET;

    // JWT token defaults
    public final String TOKEN_HEADER = "Authorization";
    public final String TOKEN_PREFIX = "Bearer ";
    public final String TOKEN_TYPE = "JWT";
    public final String TOKEN_ISSUER = "unicast-api";
    public final String TOKEN_AUDIENCE = "unicast-clients";

    public SecurityConstants(@Value("${unicast.security.jwt-secret}") String secret) {
        JWT_SECRET = secret;
    }

}