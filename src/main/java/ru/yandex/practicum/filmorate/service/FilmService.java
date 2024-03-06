package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

@Service
@Slf4j
public class FilmService extends InMemoryFilmStorage {

    public Film addLike(Integer filmId, Integer userId) {
        Film film = getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        film.getLikes().add(userId);
        update(film);
        return film;
    }

    public Film removeLike(Integer id, Integer userId) {
        Film film = getFilm(id);
        if (film == null || userId < 0) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        film.getLikes().add(userId);
        update(film);
        return film;
    }

    public List<Film> mostPopular(Integer count) {
        if (getAllFilms().size() == 0) {
            throw new NotFoundException("Объекты класса Film не найдены");
        }
        return getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
