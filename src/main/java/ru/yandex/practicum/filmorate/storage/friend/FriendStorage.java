package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendStorage {
    User addFriend(Integer id, Integer friendId);

    Set<User> getAllFriends(Integer id);

    User removeFriend(Integer id, Integer removeId);

    List<User> getAllMutualFriends(Integer id, Integer otherId);

}
