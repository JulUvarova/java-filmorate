package ru.yandex.practicum.filmorate.exception;

public class IncorrectRequestParam extends RuntimeException {
    public IncorrectRequestParam(String message) {
        super(message);
    }
}
