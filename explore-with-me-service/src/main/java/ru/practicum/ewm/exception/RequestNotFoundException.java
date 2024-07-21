package ru.practicum.ewm.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String s) {
        super(s);
    }
}
