package ru.yandex.practicum.filmorate.exceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class ExceptionAnswer {

    public static Map<String, String> exceptionAnswer(String error, String msg) {
        Map<String, String> answer = new HashMap<>();
        answer.put(error, msg);
        return answer;
    }
}
