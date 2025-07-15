package com.hashjosh.agripro.config.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "mail")
public record MailPropertiesDto(
         String  host,
         Integer port,
         String username,
         String password,
         Properties properties
) {
    public record Properties(
        SMTPProperties smtpProperties
    ){
        public record SMTPProperties(
                boolean auth,
                StartTlsProperties tlsProperties
        ){
           public record StartTlsProperties(
                   boolean enable
           )
        }

    }
}
