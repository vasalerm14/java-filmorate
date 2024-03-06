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
public class FilmService extends InMemoryFilmStorage{

    public Film addLike(Integer filmId, Integer userId) {
        if(getFilm(filmId)==null){
            throw new NotFoundException("Объект класса Film не найден");
        }
        getFilm(filmId).getLikes().add(userId);
        return getFilm(filmId);
    }

    public Film removeLike(Integer id, Integer userId) {
        if (getFilm(id) == null || userId < 0) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        getFilm(id).getLikes().add(userId);
        return getFilm(id);
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
