package ru.yandex.practicum.filmorate.storage.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;

import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        try {
            String addFriendToUser = "INSERT INTO Friendship (user_id, friend_id) VALUES (?, ?)";
            jdbcTemplate.update(addFriendToUser, id, friendId);
            String sqlGet = "SELECT * FROM USERS WHERE id = ?";
            User user = jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, id);
            user.setFriends(getFriendFromDB(user));
            return user;
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Set<User> getAllFriends(Integer id) {
        if (checkUser(id)) {
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
        if (checkUser(id) && checkUser(removeId)) {
            String deleteQuery = "DELETE FROM Friendship WHERE (user_id = ? AND friend_id = ?)";
            jdbcTemplate.update(deleteQuery, id, removeId);
            String sqlGet = "SELECT * FROM USERS WHERE id = ?";
            return jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, id);
        }
        throw new NotFoundException("Пользователь не найден");

    }

    @Override
    public Set<User> getAllMutualFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT u.* " +
                "FROM Users u " +
                "JOIN Friendship f1 ON u.id = f1.friend_id " +
                "JOIN Friendship f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, new Object[]{id, otherId}, this::mapRowToUser));
    }


    public boolean checkUser(Integer id) {
        String sqlGet = "SELECT * FROM USERS WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, id);
        return (user != null);
    }


    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException, SQLException {
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
            Set<Integer> friendIdsSet = new HashSet<>(friendIds);
            return friendIdsSet;
        }
        return null;
    }
}
