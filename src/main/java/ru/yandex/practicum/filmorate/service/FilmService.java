package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Service
@Slf4j
public class FilmService {
    public InMemoryFilmStorage inMemoryFilmStorage;
    public InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilm(filmId);
        if (film == null || inMemoryUserStorage.getUser(userId) == null) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        film.getLikes().add(userId);
        inMemoryFilmStorage.update(film);
        return film;
    }

    public Film removeLike(Integer id, Integer userId) {
        Film film = inMemoryFilmStorage.getFilm(id);
        if (film == null || inMemoryUserStorage.getUser(userId) == null) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        film.getLikes().add(userId);
        inMemoryFilmStorage.update(film);
        return film;
    }

    public List<Film> mostPopular(Integer count) {
        if (inMemoryFilmStorage.getAllFilms().size() == 0) {
            throw new NotFoundException("Объекты класса Film не найдены");
        }
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
