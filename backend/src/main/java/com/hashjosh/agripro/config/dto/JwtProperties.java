package com.hashjosh.agripro.config.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        String expiration,
        String cookieName
) {

}
