package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
@Slf4j
@RestController
public class FilmController {
    Integer id = 1;
    private Map<Integer,Film> films = new HashMap<>();

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {

        log.debug("Получен запрос POST /films");
        if(validation(film)){
            film.setId(id++);
            films.put(film.getId(),film);
        }
        else{
            throw new ValidationException("Ошибка валидации");
        }

        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film){
        log.debug("Получен запрос PUT /films");

        if(films.containsKey(film.getId()) && validation(film)){
            films.put(film.getId(),film);
        }
        else if(validation(film)){
            throw new ValidationException("Фильм не найден");
        }
        else{
            log.warn("Ошибка валидации PUT /films");
            throw new ValidationException("Ошибка валидации");
        }


        return film;
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms(){
        log.debug("Получен запрос GET /films");
        return films.values();
    }

    private boolean validation(Film film){
        LocalDate cutoffDate = LocalDate.of(1895, 12, 28);
        if(!film.getName().isEmpty() &&
                film.getDescription().length() <= 200 &&
                film.getReleaseDate().isAfter(cutoffDate) &&
                film.getDuration() > 0){
            return true;
        }
        throw new ValidationException("Ошибка валидации");

    }
}
