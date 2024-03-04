package ru.yandex.practicum.filmorate.ExceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidation(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Ошибка валидации");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Объект не найден");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(final Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Возникло исключение");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

