package ru.practicum.ewm.exception;

public class NotValidRequestException extends RuntimeException {
    public NotValidRequestException(String s) {
        super(s);
    }
}
