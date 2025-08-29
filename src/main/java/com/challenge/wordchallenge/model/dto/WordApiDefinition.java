package com.challenge.wordchallenge.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class WordApiDefinition {
    private String word;
    private List<Result> results;

    @Data
    public static class Result {
        private String definition;
        private String partOfSpeech;
    }
}
