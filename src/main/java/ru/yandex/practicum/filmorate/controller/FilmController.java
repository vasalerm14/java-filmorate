package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class FilmController {
    private final LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);
    private Integer id = 1;
    private Map<Integer, Film> films = new HashMap<>();

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");
        validation(film);
        film.setId(id++);
        log.info("Фильм c id: {} добавлен в библиотеку POST /films",film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");
        validation(film);
        if (films.containsKey(film.getId())) {
            log.info("Фильм c id: {} обновлен PUT /films",film.getId());
            films.put(film.getId(), film);
            return film;
        }
        log.info("Фильм не найден PUT /films");
        throw new ValidationException("Фильм не найден в библиотеке");
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.debug("Получен запрос GET /films");
        return List.copyOf(films.values());
    }

    public void validation(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("Ошибка валидации");
            throw new ValidationException("Пустое имя фильма");
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.warn("Ошибка валидации");
            throw new ValidationException("Слишком длинное описание");
        } else if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(firstFilmDate)) {
            log.warn("Ошибка валидации");
            throw new ValidationException("Некоректная дата релиза");
        } else if (film.getDuration() == null || film.getDuration() < 0) {
            log.warn("Ошибка валидации");
            throw new ValidationException("Некорректная длина фильма");
        }
    }
}
