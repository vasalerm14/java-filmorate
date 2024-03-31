package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = new UserDbStorage(jdbcTemplate);
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        try {
            String addFriendToUser = "INSERT INTO Friendship (user_id, friend_id) VALUES (?, ?)";
            jdbcTemplate.update(addFriendToUser, id, friendId);
            String sqlGet = "SELECT * FROM Users WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, id);
            user.setFriends(getFriendFromDB(user));
            return user;
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Set<User> getAllFriends(Integer id) {
        if (userDbStorage.checkUser(id)) {
            String sqlQuery = "SELECT u.* " +
                    "FROM Users u " +
                    "JOIN Friendship f ON u.id = f.friend_id " +
                    "WHERE f.user_id = ?";
            return new HashSet<>(jdbcTemplate.query(sqlQuery, new Object[]{id}, this::mapRowToUser));
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public User removeFriend(Integer id, Integer removeId) {
        if (userDbStorage.checkUser(id) && userDbStorage.checkUser(removeId)) {
            String deleteQuery = "DELETE FROM Friendship WHERE (user_id = ? AND friend_id = ?)";
            jdbcTemplate.update(deleteQuery, id, removeId);
            return userDbStorage.getUser(id);
        }
        throw new NotFoundException("Пользователь не найден");

    }

    @Override
    public List<User> getAllMutualFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT u.* " +
                "FROM Users u " +
                "JOIN Friendship f1 ON u.id = f1.friend_id " +
                "JOIN Friendship f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sqlQuery, new Object[]{id, otherId}, this::mapRowToUser);
    }


    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }

    private Set<Integer> getFriendFromDB(User user) {
        if (user != null) {
            List<Integer> friendIds = jdbcTemplate.queryForList(
                    "SELECT friend_id FROM Friendship WHERE user_id = ?",
                    Integer.class, user.getId());
            return new HashSet<>(friendIds);
        }
        return null;
    }
}
