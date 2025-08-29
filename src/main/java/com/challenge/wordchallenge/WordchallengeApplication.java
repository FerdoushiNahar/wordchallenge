package com.challenge.wordchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WordchallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordchallengeApplication.class, args);
	}

}
