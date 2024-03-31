package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("USERS").usingGeneratedKeyColumns("id");
        int key = simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue();
        user.setId(key);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET " + "email = ?, login = ?, name = ?, birthday = ? " + "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        String sqlGet = "SELECT * FROM USERS WHERE id = ?";
        User updatedUser = jdbcTemplate.queryForObject(sqlGet, this::mapRowToUser, user.getId());
        if (updatedUser == null) {
            throw new NotFoundException("Пользователь с id: " + user.getId() + " не найден");
        }
        return updatedUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM Users";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        for (User user : users) {
            user.setFriends(getFriendFromDB(user));
        }

        return users;
    }

    @Override
    public User getUser(Integer id) {
        String sqlQuery = "SELECT u.*, f.friend_id " + "FROM Users u " + "LEFT JOIN Friendship f ON u.id = f.user_id " + "WHERE u.id = ?";
        User user;
        user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден");
        }
        user.setFriends(getFriendFromDB(user));

        return user;
    }

    public boolean checkUser(Integer id) {
        User user = getUser(id);
        return (user != null);
    }


    public Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", null);
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
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
            List<Integer> friendIds = jdbcTemplate.queryForList("SELECT friend_id FROM Friendship WHERE user_id = ?", Integer.class, user.getId());
            Set<Integer> friendIdsSet = new HashSet<>(friendIds);
            return friendIdsSet;
        }
        return null;

    }

}