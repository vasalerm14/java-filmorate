package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;
    private FriendStorage friendStorage;

    @Autowired
    public UserService(UserDbStorage userStorage, FriendDbStorage friendDbStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendDbStorage;
    }


    public User create(User user) {
        return userStorage.create(user);
    }


    public User update(User user) {
        return userStorage.update(user);

    }


    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public User addFriend(Integer id, Integer friendId) {
        return friendStorage.addFriend(id, friendId);
    }

    public User removeFriend(Integer id, Integer removeId) {

        return friendStorage.removeFriend(id, removeId);
    }

    public Set<User> getAllFriends(Integer id) {
        return friendStorage.getAllFriends(id);
    }

    public Set<User> getAllMutualFriends(Integer id, Integer otherId) {
        return friendStorage.getAllMutualFriends(id, otherId);
    }
}
