package com.challenge.wordchallenge.service;

import com.challenge.wordchallenge.exception.custom.ApiClientException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class RandomWordClient {

    private final WebClient randomWordWebClient;

    public RandomWordClient(@Qualifier("randomWordWebClient") WebClient randomWordWebClient) {
        this.randomWordWebClient = randomWordWebClient;
    }

    @Value("${wordchallenge.randomWordApiBaseUrl}")
    private String baseUrl;

    /** Calls GET /word which returns a JSON array like ["unlaced"]. */
    public String fetchRandomWord() {
        try {
            List<String> resp = randomWordWebClient
                    .get()
                    .uri("/word")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .block();
            if (resp == null || resp.isEmpty()) {
                throw new ApiClientException("Random Word API returned empty response");
            }
            return resp.get(0);
        } catch (Exception e) {
            throw new ApiClientException("Failed to fetch random word", e);
        }
    }
}
