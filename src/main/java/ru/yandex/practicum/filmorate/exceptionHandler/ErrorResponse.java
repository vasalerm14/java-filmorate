package ru.yandex.practicum.filmorate.exceptionHandler;

public class ErrorResponse {

    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse fromException(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
