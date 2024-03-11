package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
@Slf4j
public class FilmService implements FilmStorage {
    private FilmStorage inMemoryFilmStorage;
    private UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film film) {
        return inMemoryFilmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @Override
    public void validation(Film film) {
        inMemoryFilmStorage.validation(film);
    }

    @Override
    public Film getFilm(Integer id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public Film getFilm(int id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilm(filmId);
        if (film == null || inMemoryUserStorage.getUser(userId) == null) {
            throw new NotFoundException("Объект класса Film не найден");
        }
        film.getLikes().add(userId);
        return film;
    }

    public Film removeLike(Integer id, Integer userId) {
        Film film = inMemoryFilmStorage.getFilm(id);
        if (film == null || inMemoryUserStorage.getUser(userId) == null) {

            throw new NotFoundException("Объект класса Film не найден");
        }
        film.getLikes().add(userId);
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
