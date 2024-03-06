package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService extends InMemoryUserStorage {

    public User addFriend(Integer id, Integer friendId) {
        User user = getUser(id);
        User user1 = getUser(friendId);
        if (getUser(id) == null || getUser(friendId) == null) {
            throw new NotFoundException("Объект класса User не найден");
        }
        user.getFriends().add(friendId);
        user1.getFriends().add(id);
        update(user);
        update(user1);
        return user;
    }

    public User removeFriend(Integer id, Integer removeId) {
        User user = getUser(id);
        User user1 = getUser(removeId);
        if (getUser(id) == null || getUser(removeId) == null) {
            throw new NotFoundException("Объект класса User не найден");
        }
        user.getFriends().remove(removeId);
        user1.getFriends().remove(id);
        update(user);
        update(user1);
        return user;
    }

    public Set<User> getAllFriends(Integer id) {
        Set<User> friends = new LinkedHashSet<>();
        for (Integer friendId : getUser(id).getFriends()) {
            friends.add(getUser(friendId));
        }
        return friends;
    }

    public Set<User> getAllMutualFriends(Integer id, Integer otherId) {
        if (getUser(id) == null || getUser(otherId) == null) {
            throw new NotFoundException("Объекты класса User не найдены");
        }
        Set<User> mutualFriends = new HashSet<>();
        for (Integer friendId : getUser(id).getFriends()) {
            if (getUser(otherId).getFriends().contains(friendId)) {
                mutualFriends.add(getUser(friendId));
            }
        }
        return mutualFriends;
    }


}
