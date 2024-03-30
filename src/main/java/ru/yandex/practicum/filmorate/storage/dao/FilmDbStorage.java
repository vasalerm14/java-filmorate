package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j

public class FilmDbStorage implements FilmStorage {
    //Привет, оставил комментарии тут, я написал вопрос в пачке, в следующий раз напишу их где скажешь
    //1: Извини за такой кривой код, очень тороплюсь тк к 9:00 в пн надо сдать все, сидел более 11 часов делал это
    //2: Тесты я еще не писал тк не уверен в структуре проекта
    //3: Правильна ли структура классов Film и User с учетом новых полей
    //4: Если есть возможность обрати внимание прям на все чтоб я сразу кучу правок внес и желательно больше пояснений)
    //Заранее спасибо
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("FILM")
                    .usingGeneratedKeyColumns("id");
            int key = simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue();
            film.setId(key);
            updateGenre(film.getGenres(), film.getId());
            return film;
        } catch (RuntimeException e) {
            throw new ValidationException("Ошибка валидации");
        }
    }


    @Override
    public Film update(Film film) {
        try {
            String sqlQuery = "UPDATE FILM SET " +
                    "name = ?, description = ?, releaseDate = ?, duration = ? ,mpaRating_id = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            String sqlGet = "SELECT * FROM FILM WHERE id = ?";
            Film updateFilm = jdbcTemplate.queryForObject(sqlGet, this::mapRowToFilm, film.getId());
            updateGenre(film.getGenres(), film.getId());
            return updateFilm;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM FILM";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        return films;
    }

    @Override
    public void validation(Film film) {
    }

    @Override
    public Film getFilm(Integer id) {

        try {
            String sqlQuery = "SELECT * FROM FILM WHERE ID = ?";

            Film film;
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    @Override
    public List<Film> mostPopular(Integer count) {
        String sql = "SELECT " +
                "f.id, " +
                "f.name, " +
                "f.description, " +
                "f.releaseDate, " +
                "f.duration, " +
                "m.id AS mpa_id, " +
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

        List<Film> popularFilms = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm2(rs, rowNum), count);
        return popularFilms;
    }

    public Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", null);
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("releaseDate", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpaRating_id", film.getMpa().getId());
        return values;
    }


    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        Integer mpaRatingId = resultSet.getInt("mpaRating_id");
        MpaDao mpaDao = new MpaDao(jdbcTemplate);
        MPA mpa = mpaDao.getMPAById(mpaRatingId);
        film.setGenres(getGenresByFilmId(film.getId()));
        film.setMpa(mpa);
        LikesDao likesDao = new LikesDao(jdbcTemplate);
        film.setLikes(likesDao.getUsersLikedFilm(film.getId()));
        return film;
    }

    private Film mapRowToFilm2(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Integer mpaRatingId = rs.getInt("mpa_id");
        MpaDao mpaDao = new MpaDao(jdbcTemplate);
        MPA mpa = mpaDao.getMPAById(mpaRatingId);
        film.setMpa(mpa);
        film.setGenres(getGenresByFilmId(film.getId()));
        film.setMpa(mpa);
        LikesDao likesDao = new LikesDao(jdbcTemplate);
        film.setLikes(likesDao.getUsersLikedFilm(film.getId()));
        return film;
    }

    public Collection<Genre> getGenresByFilmId(Integer filmId) {
        String sql = "SELECT g.id, g.name FROM Genre g " +
                "JOIN FilmGenre fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, filmId);
    }


    private void updateGenre(Set<Genre> genres, int id) {
        jdbcTemplate.update("DELETE FROM FILMGENRE WHERE film_id = ?", id);
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO FilmGenre (film_id, genre_id) VALUES (?, ?)", id, genre.getId());
        }
    }

}
