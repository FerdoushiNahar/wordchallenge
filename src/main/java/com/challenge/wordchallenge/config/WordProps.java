package com.challenge.wordchallenge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wordchallenge")
public record WordProps(
        String randomWordApiBaseUrl,
        String dictionaryApiBaseUrl,
        String language,
        String timezone
) {}
