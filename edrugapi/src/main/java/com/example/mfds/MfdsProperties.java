package com.example.mfds;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mfds.api")
public record MfdsProperties(
        String baseUrl,
        String serviceKey,
        Integer connectTimeoutMs,
        Integer readTimeoutMs
) {}
