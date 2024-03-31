package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class GenreDb implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM Genre WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    public Collection<Genre> getAllGenres() {
        String sql = "SELECT * FROM Genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }


}
