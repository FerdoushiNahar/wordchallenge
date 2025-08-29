package com.challenge.wordchallenge.persistence.repository;

import com.challenge.wordchallenge.model.domain.WordEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WordEntryRepository extends JpaRepository<WordEntry, Long> {
    Optional<WordEntry> findByDate(LocalDate date);
}
