package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;


import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Likes.Likes;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmDbStorage;
    private Likes likes;


    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, LikesDao likesDao) {
        this.filmDbStorage = filmDbStorage;
        this.likes = likesDao;
    }

    public Film create(Film film) {
        return filmDbStorage.create(film);
    }

    public Film update(Film film) {
        return filmDbStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmDbStorage.getFilm(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        return likes.addLike(filmId, userId);
    }

    public Film removeLike(Integer id, Integer userId) {
        return likes.removeLike(id, userId);
    }

    public List<Film> mostPopular(Integer count) {
        return filmDbStorage.mostPopular(count);
    }
}
