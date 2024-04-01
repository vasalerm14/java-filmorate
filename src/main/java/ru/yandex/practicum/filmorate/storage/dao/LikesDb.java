package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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
        String sqlQuery = "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration, " +
                "m.id as mpa_id, " +
                "m.name AS mpa_name, " +
                "COALESCE(l.like_count, 0) AS like_count " +
                "FROM " +
                "FILM f " +
                "LEFT JOIN " +
                "MPA m ON f.mpaRating_id = m.id " +
                "LEFT JOIN " +
                "(SELECT film_id, COUNT(*) AS like_count FROM Likes GROUP BY film_id) l ON f.id = l.film_id " +
                "WHERE f.id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        String sqlDelete = "DELETE FROM Likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlDelete, userId, userId);
        String sqlQuery = "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration, " +
                "m.id as mpa_id, " +
                "m.name AS mpa_name, " +
                "COALESCE(l.like_count, 0) AS like_count " +
                "FROM " +
                "FILM f " +
                "LEFT JOIN " +
                "MPA m ON f.mpaRating_id = m.id " +
                "LEFT JOIN " +
                "(SELECT film_id, COUNT(*) AS like_count FROM Likes GROUP BY film_id) l ON f.id = l.film_id " +
                "WHERE f.id = ?";
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
        film.setLikeCount(resultSet.getInt("like_count"));
        int mpaId = (resultSet.getInt("mpa_id"));
        String mpaName = (resultSet.getString("mpa_name"));
        film.setMpa(new MPA(mpaId, mpaName));
        Map<Integer, List<Genre>> allGenres = getAllFilmGenre();
        if (allGenres.get(film.getId()) != null) {
            film.setGenres(allGenres.get(film.getId()));
        }
        return film;
    }

    public Map<Integer, List<Genre>> getAllFilmGenre() {
        String sqlQuery = "SELECT fg.film_id, g.id AS genre_id, g.name AS genre_name " +
                "FROM FilmGenre fg " +
                "JOIN Genre g ON fg.genre_id = g.id";

        Map<Integer, List<Genre>> filmGenreMap = new HashMap<>();

        jdbcTemplate.query(sqlQuery, (resultSet, rowNum) -> {
            int filmId = resultSet.getInt("film_id");
            int genreId = resultSet.getInt("genre_id");
            String genreName = resultSet.getString("genre_name");

            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(genreName);

            filmGenreMap.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);

            return null;
        });
        return filmGenreMap;
    }

    public Set<Integer> getUsersLikedFilm(int filmId) {
        String sql = "SELECT user_id FROM Likes WHERE film_id = ?";
        List<Integer> userIds = jdbcTemplate.queryForList(sql, Integer.class, filmId);
        return new HashSet<>(userIds);
    }

    public Map<Integer, Set<Integer>> getAllLikes() {
        String sqlQuery = "SELECT film_id, user_id FROM Likes";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery);

        Map<Integer, Set<Integer>> likesMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            int filmId = (int) row.get("film_id");
            int userId = (int) row.get("user_id");

            likesMap.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
        }

        return likesMap;
    }

    @Override
    public List<Film> mostPopular(int count) {
        String sqlQuery = "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration, " +
                "m.id as mpa_id, " +
                "m.name AS mpa_name, " +
                "COALESCE(l.like_count, 0) AS like_count " +
                "FROM " +
                "FILM f " +
                "LEFT JOIN " +
                "MPA m ON f.mpaRating_id = m.id " +
                "LEFT JOIN " +
                "(SELECT film_id, COUNT(*) AS like_count FROM Likes GROUP BY film_id) l ON f.id = l.film_id " +
                "ORDER BY like_count DESC " +
                "LIMIT ?";
        List<Film> popularFilms = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToFilm(rs, rowNum), count);
        return popularFilms;
    }
}
