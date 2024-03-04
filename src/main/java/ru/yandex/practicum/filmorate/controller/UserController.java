package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Пользователь добавлен с id: {} POST /users",user.getId());
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
            log.info("Пользователь с id: {} успешно обновлен PUT /users",user.getId());
            users.put(user.getId(), user);
            return user;
        }
        throw new ValidationException("Пользователь не найден");
    }


    @GetMapping("/users")
    public Collection<User> getAllFilms() {
        log.debug("Получен запрос GET /users");
        return List.copyOf(users.values());
    }


    public void validation(User user) {
        LocalDate today = LocalDate.now();
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("Некорректный email");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(today)) {
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}
