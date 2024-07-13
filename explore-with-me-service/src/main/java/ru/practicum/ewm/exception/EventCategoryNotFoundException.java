package ru.practicum.ewm.exception;

public class EventCategoryNotFoundException extends RuntimeException {
    public EventCategoryNotFoundException(String s) {
        super(s);
    }
}
