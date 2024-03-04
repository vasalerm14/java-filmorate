package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer id = 1;

    private Map<Integer, User> users = new HashMap<>();


    @Override
    public User create(User user) {
        validation(user);
        user.setId(id++);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public Collection<User> getAllFilms() {
        return List.copyOf(users.values());
    }

    @Override
    public void validation(User user) {
        LocalDate today = LocalDate.now();
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            System.out.println("1");
            throw new ValidationException("Некорректный email");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            System.out.println("1");
            throw new ValidationException("Некорректный логин");
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(today)) {
            System.out.println("1");
            throw new ValidationException("Некорректная дата рождения");
        }
    }

    public User getUser(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }

    public boolean containUser(Integer id) {
        return users.containsKey(id);
    }
}
