package com.challenge.wordchallenge.service;

import com.challenge.wordchallenge.model.domain.WordDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for WordsApiClient using a stubbed WebClient (no real network).
 */
class WordsApiClientTest {

    @Test
    void fetchDefinitions_parsesMeaningsAndDefinitions() {
        String body = """
        [
          {
            "word": "test",
            "meanings": [
              { "partOfSpeech": "noun", "definitions": [ { "definition": "a procedure" } ] },
              { "partOfSpeech": "verb", "definitions": [ { "definition": "to try" } ] }
            ]
          }
        ]
        """;

        WebClient webClient = stubbedWebClient(HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE, body);
        WordsApiClient client = new WordsApiClient(webClient);

        List<WordDefinition> defs = client.fetchDefinitions("test", "en");

        assertThat(defs).hasSize(2);
        assertThat(defs.get(0).getDefinition()).isEqualTo("a procedure");
        assertThat(defs.get(0).getPartOfSpeech()).isEqualTo("noun");
        assertThat(defs.get(1).getDefinition()).isEqualTo("to try");
        assertThat(defs.get(1).getPartOfSpeech()).isEqualTo("verb");
    }

    @Test
    void fetchDefinitions_when404_returnsEmptyList() {
        WebClient webClient = stubbedWebClient(HttpStatus.NOT_FOUND, MediaType.APPLICATION_JSON_VALUE, "{}");
        WordsApiClient client = new WordsApiClient(webClient);

        List<WordDefinition> defs = client.fetchDefinitions("missingword", "en");

        assertThat(defs).isEmpty();
    }

    // ---- helper to create a WebClient that returns a canned response ----
    private WebClient stubbedWebClient(HttpStatus status, String contentType, String body) {
        ExchangeFunction fx = (ClientRequest req) -> {
            ClientResponse resp = ClientResponse.create(status)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    // FIX: Spring 6 only supports the String overload here
                    .body(body == null ? "" : body)
                    .build();
            return Mono.just(resp);
        };
        return WebClient.builder().exchangeFunction(fx).build();
    }
}
