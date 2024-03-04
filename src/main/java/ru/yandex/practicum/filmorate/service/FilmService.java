package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.LinkedHashSet;

@Service
@Slf4j
public class FilmService {

    public Film addLike(Film film, Integer userId) {
        if (film == null) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        Set<Integer> oldLikes = film.getLikes();
        oldLikes.add(userId);
        film.setLikes(oldLikes);
        return film;
    }

    public Film removeLike(Film film, Integer userId) {
        if (film == null || userId < 0) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        Set<Integer> oldLikes = film.getLikes();
        oldLikes.remove(userId);
        film.setLikes(oldLikes);
        return film;
    }

    public Set<Film> mostPopular(Collection<Film> films, Integer count) {
        if (films.size() == 0) {
            throw new NotFoundException("Объекты класса Film не найдены");
        }
        Set<Film> sortedFilms = films.stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sortedFilms.stream()
                .limit(count)
                .collect(Collectors.toSet());
    }


}
