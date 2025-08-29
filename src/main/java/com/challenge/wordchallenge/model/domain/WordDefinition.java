package com.challenge.wordchallenge.model.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordDefinition {
    @NotBlank
    private String definition;
    private String partOfSpeech;
}
