package com.challenge.wordchallenge.model.dto;

import com.challenge.wordchallenge.model.domain.WordDefinition;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordResponse {
    private String word; // include the word
    private List<WordDefinition> definitions; // list of {definition, partOfSpeech}
}
