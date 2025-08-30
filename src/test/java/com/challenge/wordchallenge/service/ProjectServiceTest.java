package com.challenge.wordchallenge.service;

import com.challenge.wordchallenge.exception.custom.NotFoundException;
import com.challenge.wordchallenge.model.domain.WordDefinition;
import com.challenge.wordchallenge.model.dto.WordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private RandomWordClient randomWordClient;
    private WordsApiClient wordsApiClient;
    private ProjectService service;

    @BeforeEach
    void setUp() {
        randomWordClient = mock(RandomWordClient.class);
        wordsApiClient = mock(WordsApiClient.class);
        service = new ProjectService(randomWordClient, wordsApiClient);
        ReflectionTestUtils.setField(service, "language", "en");
    }

    @Test
    void getTodayWord_success_returnsWordAndDefinitions() {
        when(randomWordClient.fetchRandomWord()).thenReturn("unlaced");

        List<WordDefinition> defs = List.of(
                WordDefinition.builder().definition("not under constraint in action or expression").partOfSpeech("adjective").build(),
                WordDefinition.builder().definition("with laces not tied").partOfSpeech("adjective").build()
        );
        when(wordsApiClient.fetchDefinitions("unlaced", "en")).thenReturn(defs);

        WordResponse resp = service.getTodayWord();

        assertThat(resp.getWord()).isEqualTo("unlaced");
        assertThat(resp.getDefinitions()).hasSize(2);
        verify(randomWordClient, atLeastOnce()).fetchRandomWord();
        verify(wordsApiClient, atLeastOnce()).fetchDefinitions("unlaced", "en");
    }

    @Test
    void getTodayWord_retriesAndThrows_whenNoDefinitionsFound() {
        when(randomWordClient.fetchRandomWord()).thenReturn("nonexistent");
        when(wordsApiClient.fetchDefinitions(anyString(), eq("en"))).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getTodayWord())
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Could not find a random word with definitions");

        verify(randomWordClient, atLeast(1)).fetchRandomWord();
        verify(wordsApiClient, atLeast(1)).fetchDefinitions(anyString(), eq("en"));
    }
}
