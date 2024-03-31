package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikesDb;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private LikesStorage likes;


    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, LikesDb likesDb) {
        this.filmStorage = filmDbStorage;
        this.likes = likesDb;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        return likes.addLike(filmId, userId);
    }

    public Film removeLike(Integer id, Integer userId) {
        return likes.removeLike(id, userId);
    }

}
