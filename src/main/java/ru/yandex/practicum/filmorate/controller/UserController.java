package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.ExceptionHandler.GlobalExceptionHandler;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@Controller
@ControllerAdvice(basePackageClasses = GlobalExceptionHandler.class)
public class UserController {

    private final UserService userService;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(UserService userService, InMemoryUserStorage inMemoryUserStorage) {
        this.userService = userService;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");
        return inMemoryUserStorage.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");
        return inMemoryUserStorage.update(user);
    }


    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return inMemoryUserStorage.getAllFilms();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return inMemoryUserStorage.getUser(id);
    }


    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.addFriend(inMemoryUserStorage.getUser(id), friendId, inMemoryUserStorage);
        inMemoryUserStorage.update(user);
        return user;
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.removeFriend(inMemoryUserStorage.getUser(id), friendId);
        inMemoryUserStorage.update(user);
        return user;
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getAllFriends(@PathVariable int id) {
        return userService.getAllFriends(inMemoryUserStorage.getUser(id), inMemoryUserStorage);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getAllMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getAllMutualFriends(inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(otherId), inMemoryUserStorage);
    }


}
