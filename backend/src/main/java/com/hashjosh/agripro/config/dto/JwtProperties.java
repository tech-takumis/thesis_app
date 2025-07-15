package com.hashjosh.agripro.config.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        CookieProperties cookie,
        String secret,
        String expiration
) {
    public record CookieProperties(
            String name
    ){
    }
}
