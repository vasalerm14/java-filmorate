package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class LikesDb implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        String sqlInsert = "INSERT INTO Likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsert, filmId, userId);
        String sqlQuery = "SELECT * FROM FILM WHERE ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }


    @Override
    public Film removeLike(Integer id, Integer userId) {
        String sqlDelete = "DELETE FROM Likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlDelete, userId, userId);
        String sqlQuery = "SELECT * FROM FILM WHERE ID = ?";
        Film film;
        film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        return film;
    }


    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        Integer mpaRatingId = resultSet.getInt("mpaRating_id");
        MpaDb mpaDao = new MpaDb(jdbcTemplate);
        MPA mpa = mpaDao.getMPAById(mpaRatingId);

        film.setMpa(mpa);
        film.setLikes(getUsersLikedFilm(film.getId()));
        return film;
    }

    public Set<Integer> getUsersLikedFilm(int filmId) {
        String sql = "SELECT user_id FROM Likes WHERE film_id = ?";
        List<Integer> userIds = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(userIds);
    }

    @Override
    public List<Film> mostPopular(Integer count) {
        String sql = "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration, " +
                "m.id AS mpaRating_id, " +
                "COALESCE(l.likes_count, 0) AS likes_count " +
                "FROM " +
                "Film f " +
                "LEFT JOIN " +
                "MPA m ON f.mpaRating_id = m.id " +
                "LEFT JOIN " +
                "(SELECT film_id, COUNT(*) AS likes_count FROM Likes GROUP BY film_id) l ON f.id = l.film_id " +
                "ORDER BY " +
                "likes_count DESC " +
                "LIMIT ?";

        List<Film> popularFilms = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs, rowNum), count);
        return popularFilms;
    }


}
