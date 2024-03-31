package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDb;

import java.util.Collection;

@RestController
public class GenreController {

    private final GenreDb genreDao;

    @Autowired
    public GenreController(GenreDb genreDao) {
        this.genreDao = genreDao;
    }

    @GetMapping("/genres")
    public Collection<Genre> getMpa() {
        return genreDao.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getMpaById(@PathVariable int id) {
        return genreDao.getGenreById(id);
    }
}
