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

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Word API")
public class WordController {

    private final ProjectService service;

    // Root now returns the word + definitions (fresh random each call)
    @Operation(summary = "Get a fresh random word with definitions (shown at /)")
    @GetMapping("/")
    public WordResponse index() {
        return service.getTodayWord();
    }

    // Keep this endpoint too (same payload, handy for API clients)
    @Operation(summary = "Get a fresh random word with definitions")
    @GetMapping("/wordOfTheDay")
    public WordResponse getWordOfTheDay() {
        return service.getTodayWord();
    }
}
