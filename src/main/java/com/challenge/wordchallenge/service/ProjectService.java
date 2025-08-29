package com.challenge.wordchallenge.service;

import com.challenge.wordchallenge.exception.custom.NotFoundException;
import com.challenge.wordchallenge.model.domain.WordDefinition;
import com.challenge.wordchallenge.model.dto.WordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final RandomWordClient randomWordClient;
    private final WordsApiClient wordsApiClient;

    @Value("${wordchallenge.language:en}")
    private String language;

    /**
     * Returns a fresh random word with definitions on every call.
     * No persistence / per-day caching.
     */
    public WordResponse getTodayWord() {
        WordPickResult pick = fetchWordWithDefinitions();

        return WordResponse.builder()
                .word(pick.word)
                .definitions(pick.definitions)
                .build();
    }

    /** Try random words and a few normalized forms until we get definitions. */
    private WordPickResult fetchWordWithDefinitions() {
        final int maxAttempts = 20;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String raw = randomWordClient.fetchRandomWord();
            if (raw == null) continue;

            for (String candidate : candidatesToTry(raw)) {
                List<WordDefinition> defs = wordsApiClient.fetchDefinitions(candidate, language);
                if (!defs.isEmpty()) {
                    return new WordPickResult(candidate, defs);
                }
            }
        }
        throw new NotFoundException("Could not find a random word with definitions after several attempts.");
    }

    /** Build a small list of word forms to try (lowercase, strip non-letters, simple singularizations). */
    private List<String> candidatesToTry(String word) {
        List<String> out = new ArrayList<>();
        String w = word.toLowerCase().trim().replaceAll("[^a-z]", "");
        if (w.isBlank()) return out;

        out.add(w);

        if (w.endsWith("ies") && w.length() > 3) {
            out.add(w.substring(0, w.length() - 3) + "y");
        }
        if (w.endsWith("sses") || w.endsWith("shes") || w.endsWith("ches") || w.endsWith("xes") || w.endsWith("zes")) {
            out.add(w.substring(0, w.length() - 2));
        }
        if (w.endsWith("s") && !w.endsWith("ss")) {
            out.add(w.substring(0, w.length() - 1));
        }

        // de-duplicate while keeping order
        List<String> dedup = new ArrayList<>();
        for (String c : out) if (!dedup.contains(c)) dedup.add(c);
        return dedup;
    }

    private record WordPickResult(String word, List<WordDefinition> definitions) {}
}
