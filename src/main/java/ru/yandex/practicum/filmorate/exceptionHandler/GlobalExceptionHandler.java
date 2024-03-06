package ru.yandex.practicum.filmorate.exceptionHandler;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends Exception {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(Exception ex) {
        return ExceptionAnswer.exceptionAnswer("error", "Ошибка валидации");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(Exception ex) {
        return ExceptionAnswer.exceptionAnswer("error", "Объект не найден");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final Exception ex) {
        return ExceptionAnswer.exceptionAnswer("error", "Возникло исключение");
    }
}

