package ru.yandex.practicum.filmorate.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
}
