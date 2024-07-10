package ru.practicum.ewm.exception;

public class BookingAccessDeniedException extends RuntimeException {
    public BookingAccessDeniedException(String s) {
        super(s);
    }
}
