package ru.practicum.ewm.exception;

public class BookingStatusCanChaneOnlyOwner extends RuntimeException {
    public BookingStatusCanChaneOnlyOwner(String s) {
        super(s);
    }
}
