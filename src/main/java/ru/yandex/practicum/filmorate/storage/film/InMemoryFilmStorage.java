package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);
    private Integer id = 1;
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        validation(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        throw new NotFoundException("Фильм не найден в библиотеке");
    }

    @Override
    public Collection<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public void validation(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Пустое имя фильма");
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Слишком длинное описание");
        } else if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(firstFilmDate)) {
            throw new ValidationException("Некоректная дата релиза");
        } else if (film.getDuration() == null || film.getDuration() < 0) {
            throw new ValidationException("Некорректная длина фильма");
        }
    }

    public Film getFilm(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }
}
