package com.challenge.wordchallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(WordProps.class) // only if you don’t use @ConfigurationPropertiesScan
public class WordConfig {

    @Bean
    public WebClient randomWordWebClient(WordProps props) {
        return WebClient.builder()
                .baseUrl(props.randomWordApiBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(1_048_576))
                        .build())
                .build();
    }

    @Bean
    public WebClient dictionaryApiWebClient(WordProps props) {
        return WebClient.builder()
                .baseUrl(props.dictionaryApiBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(2_097_152))
                        .build())
                .build();
    }
}
