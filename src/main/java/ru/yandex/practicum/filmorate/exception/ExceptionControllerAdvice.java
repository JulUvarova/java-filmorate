package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.info("Получен статус 400: {}", ex.getMessage(), ex.getStackTrace());
        return new ErrorResponse(message, ex.getCause(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(RuntimeException ex) {
        String message = ex.getMessage();
        log.info("Получен статус 404: {}", ex.getMessage(), ex.getStackTrace());
        return new ErrorResponse(message, ex.getCause(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectRequestParam.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectPathVariable(IncorrectRequestParam ex) {
        String message = ex.getMessage();
        log.info("Получен статус 400: {}", ex.getMessage(), ex.getStackTrace());
        return new ErrorResponse(message, ex.getCause(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnhandledException(Throwable ex) {
        String message = ex.getMessage();
        log.info("Получен статус 500: {}", ex.getMessage(), ex.getStackTrace());
        return new ErrorResponse(message, ex.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

//https://struchkov.dev/blog/ru/spring-boot-validation
//https://struchkov.dev/blog/ru/exception-handling-controlleradvice/