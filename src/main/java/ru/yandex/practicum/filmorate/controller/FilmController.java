package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ExceptionHandler.GlobalExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;


@Slf4j
@RestController
@ControllerAdvice(basePackageClasses = GlobalExceptionHandler.class)
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage,FilmService filmService){
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");
        return inMemoryFilmStorage.update(film);
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.debug("Получен запрос GET /films");
        return inMemoryFilmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id){
        return inMemoryFilmStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId){
        Film film = filmService.addLike(inMemoryFilmStorage.getFilm(id),userId);
        inMemoryFilmStorage.update(film);
        return film;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId){
        Film film = filmService.removeLike(inMemoryFilmStorage.getFilm(id),userId);
        inMemoryFilmStorage.update(film);
        return film;
    }

    @GetMapping("/films/popular")
    public Set<Film> getMostPopular(@RequestParam(required = false, defaultValue = "10") Integer count){
        Collection<Film> films = inMemoryFilmStorage.getAllFilms();
        return filmService.mostPopular(films,count);
    }

}
