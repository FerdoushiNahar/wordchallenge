package com.challenge.wordchallenge.service;

import com.challenge.wordchallenge.exception.custom.ApiClientException;
import com.challenge.wordchallenge.model.domain.WordDefinition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class WordsApiClient {

    private final WebClient webClient;

    public WordsApiClient(@Qualifier("dictionaryApiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @SuppressWarnings("unchecked")
    public List<WordDefinition> fetchDefinitions(String word, String language) {
        try {
            List<Map<String, Object>> entries = webClient
                    .get()
                    .uri(uri -> uri.path("/api/v2/entries/{lang}/{word}")
                            .build(language, word))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();

            List<WordDefinition> out = new ArrayList<>();
            if (entries == null) return out;

            for (Map<String, Object> e : entries) {
                List<Map<String, Object>> meanings = (List<Map<String, Object>>) e.get("meanings");
                if (meanings == null) continue;

                for (Map<String, Object> m : meanings) {
                    String pos = m.get("partOfSpeech") == null ? null : m.get("partOfSpeech").toString();
                    List<Map<String, Object>> defs = (List<Map<String, Object>>) m.get("definitions");
                    if (defs == null) continue;

                    for (Map<String, Object> d : defs) {
                        Object def = d.get("definition");
                        if (def != null && !def.toString().isBlank()) {
                            out.add(new WordDefinition(def.toString(), pos));
                        }
                    }
                }
            }
            return out;
        } catch (WebClientResponseException.NotFound nf) {
            // dictionaryapi.dev has no entry for this word -> treat as "no definitions"
            return List.of();
        } catch (Exception ex) {
            throw new ApiClientException("Failed to fetch definitions for word: " + word, ex);
        }
    }
}
