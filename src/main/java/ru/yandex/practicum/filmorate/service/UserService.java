package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService implements UserStorage {
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    @Override
    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUser(int id) {
        return inMemoryUserStorage.getUser(id);
    }

    public User addFriend(Integer id, Integer friendId) {
        User user = inMemoryUserStorage.getUser(id);
        User user1 = inMemoryUserStorage.getUser(friendId);
        if (inMemoryUserStorage.getUser(id) == null || inMemoryUserStorage.getUser(friendId) == null) {
            throw new NotFoundException("Объект класса User не найден");
        }
        user.getFriends().add(friendId);
        user1.getFriends().add(id);
        return user;
    }

    public User removeFriend(Integer id, Integer removeId) {
        User user = inMemoryUserStorage.getUser(id);
        User friend = inMemoryUserStorage.getUser(removeId);
        if (user == null || friend == null) {
            throw new NotFoundException("Объект класса User не найден");
        }
        user.getFriends().remove(removeId);
        friend.getFriends().remove(id);
        return user;
    }

    public Set<User> getAllFriends(Integer id) {
        Set<User> friends = new LinkedHashSet<>();
        for (Integer friendId : inMemoryUserStorage.getUser(id).getFriends()) {
            friends.add(inMemoryUserStorage.getUser(friendId));
        }
        return friends;
    }

    public Set<User> getAllMutualFriends(Integer id, Integer otherId) {
        User user = inMemoryUserStorage.getUser(id);
        User friend = inMemoryUserStorage.getUser(otherId);
        if (user == null || friend == null) {
            throw new NotFoundException("Объекты класса User не найдены");
        }
        Set<User> mutualFriends = new HashSet<>();
        for (Integer friendId : user.getFriends()) {
            if (friend.getFriends().contains(friendId)) {
                mutualFriends.add(inMemoryUserStorage.getUser(friendId));
            }
        }
        return mutualFriends;
    }
}
