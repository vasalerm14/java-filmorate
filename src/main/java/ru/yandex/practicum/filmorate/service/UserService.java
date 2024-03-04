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
public class UserService {

    public User addFriend(User user, Integer addId, InMemoryUserStorage inMemoryUserStorage) {
        if (user == null || !inMemoryUserStorage.containUser(addId)) {
            throw new NotFoundException("Объект класса User не найден");
        }
        Set<Integer> oldFriends = user.getFriends();
        oldFriends.add(addId);
        user.setFriends(oldFriends);
        User user2 = inMemoryUserStorage.getUser(addId);
        Set<Integer> oldFriends2 = user2.getFriends();
        oldFriends2.add(user.getId());
        user2.setFriends(oldFriends2);
        return user;
    }

    public User removeFriend(User user, Integer removeId) {
        if (user == null) {
            throw new NotFoundException("Объект класса User не найден");
        }
        Set<Integer> oldFriends = user.getFriends();
        oldFriends.remove(removeId);
        user.setFriends(oldFriends);
        return user;
    }

    public Set<User> getAllFriends(User user, InMemoryUserStorage inMemoryUserStorage) {
        Set<User> friends = new LinkedHashSet<>();
        for (Integer friendId : user.getFriends()) {
            log.warn("userId {}", friendId);
            friends.add(inMemoryUserStorage.getUser(friendId));
        }
        return friends;
    }

    public Set<User> getAllMutualFriends(User user1, User user2, InMemoryUserStorage inMemoryUserStorage) {
        if (user1 == null || user2 == null) {
            throw new NotFoundException("Объекты класса User не найдены");
        }
        Set<User> mutualFriends = new HashSet<>();
        for (Integer friendId : user1.getFriends()) {
            if (user2.getFriends().contains(friendId)) {
                mutualFriends.add(inMemoryUserStorage.getUser(friendId));

            }
        }
        return mutualFriends;
    }


}
