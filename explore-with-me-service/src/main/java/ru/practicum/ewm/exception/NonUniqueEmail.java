package ru.practicum.ewm.exception;

public class NonUniqueEmail extends RuntimeException {
    public NonUniqueEmail(String s) {
        super(s);
    }
}
