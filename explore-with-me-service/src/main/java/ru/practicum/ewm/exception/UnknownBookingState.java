package ru.practicum.ewm.exception;

public class UnknownBookingState extends RuntimeException {
    public UnknownBookingState(String s) {
        super(s);
    }
}
