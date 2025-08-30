package com.challenge.wordchallenge.model.dto;

import com.challenge.wordchallenge.model.domain.WordDefinition;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DefinitionsResponse {
    private List<WordDefinition> definitions;
}
