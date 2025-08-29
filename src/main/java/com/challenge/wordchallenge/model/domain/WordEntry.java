package com.challenge.wordchallenge.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "word_entry", uniqueConstraints = @UniqueConstraint(columnNames = "date"))
public class WordEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private LocalDate date; // the calendar day this word is for

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "word_definitions", joinColumns = @JoinColumn(name = "word_entry_id"))
    private List<WordDefinition> definitions = new ArrayList<>();
}
