package ru.practicum.ewm.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String s) {
        super(s);
    }
}
