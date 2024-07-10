package ru.practicum.ewm.exception;

public class CommentForbidden extends RuntimeException {
    public CommentForbidden(String s) {
        super(s);
    }
}
