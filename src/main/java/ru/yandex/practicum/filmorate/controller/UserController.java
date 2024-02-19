package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private Integer id = 1;

    private Map<Integer, User> users = new HashMap<>();

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");
        validation(user);
        user.setId(id++);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");
        validation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Ошибка валидации");
        }


        return user;
    }


    @GetMapping("/users")
    public Collection<User> getAllFilms() {
        log.debug("Получен запрос GET /users");
        return users.values();
    }


    private boolean validation(User user) {
        LocalDate today = LocalDate.now();
        if (user.getEmail().contains("@") && !user.getEmail().isBlank()
                && !user.getLogin().isBlank() && !user.getBirthday().isAfter(today)) {
            return true;
        }
        throw new ValidationException("Ошибка валидации");

    }
}
