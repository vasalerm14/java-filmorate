package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users");
        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users");
        return userService.update(user);
    }


    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return userService.getAllFilms();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }


    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<User> getAllFriends(@PathVariable int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getAllMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getAllMutualFriends(id, otherId);
    }


}
