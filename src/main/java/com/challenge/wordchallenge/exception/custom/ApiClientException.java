package com.challenge.wordchallenge.exception.custom;

public class ApiClientException extends RuntimeException {
    public ApiClientException(String message) {
        super(message);
    }

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
