package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Collection<Film> getAllFilms();

    void validation(Film film);

    Film getFilm(Integer id);


}
