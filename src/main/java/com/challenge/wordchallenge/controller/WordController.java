package com.challenge.wordchallenge.controller;

import com.challenge.wordchallenge.model.dto.WordResponse;
import com.challenge.wordchallenge.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Word API")
public class WordController {

    private final ProjectService service;

    // Friendly landing response for "/"
    @GetMapping("/")
    public Map<String, Object> index() {
        return Map.of(
                "name", "Word Challenge API",
                "endpoints", new String[]{"/wordOfTheDay", "/h2-console", "/swagger-ui/index.html"},
                "hint", "Call GET /wordOfTheDay to fetch a fresh random word with definitions."
        );
    }

    @Operation(summary = "Get a random word with definitions (fresh on every call)")
    @GetMapping("/wordOfTheDay")
    public WordResponse getWordOfTheDay() {
        return service.getTodayWord(); // now returns a fresh random word each time
    }
}
