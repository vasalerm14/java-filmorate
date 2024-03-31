package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

@Component
@Slf4j

public class FilmDbStorage implements FilmStorage {
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
        if (updateFilm == null) {
            throw new NotFoundException("Фильм с id: " + film.getId() + " не найден");
        }
        updateGenre(film.getGenres(), film.getId());
        return updateFilm;
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
        String sqlQuery = "SELECT * FROM FILM WHERE ID = ?";
        Film film;
        film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        if (film == null) {
            throw new NotFoundException("Фильм с id: " + id + " не найден");
        }
        return film;
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
        MpaDb mpaDao = new MpaDb(jdbcTemplate);
        MPA mpa = mpaDao.getMPAById(mpaRatingId);
        film.setGenres(getGenresByFilmId(film.getId()));
        film.setMpa(mpa);
        LikesDb likesDb = new LikesDb(jdbcTemplate);
        film.setLikes(likesDb.getUsersLikedFilm(film.getId()));
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
        String deleteQuery = "DELETE FROM FILMGENRE WHERE film_id = ?";
        String insertQuery = "INSERT INTO FilmGenre (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(deleteQuery, id);
        List<Object[]> batchArgs = new ArrayList<>();
        for (Genre genre : genres) {
            batchArgs.add(new Object[]{id, genre.getId()});
        }
        jdbcTemplate.batchUpdate(insertQuery, batchArgs);
    }


}
