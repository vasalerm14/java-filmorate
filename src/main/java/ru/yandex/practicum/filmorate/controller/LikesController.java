package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.GenreDb;
import ru.yandex.practicum.filmorate.storage.dao.LikesDb;

import java.util.List;
@RestController
public class LikesController {

    private final LikesDb likesDb;

    @Autowired
    public LikesController(LikesDb likesDb) {
        this.likesDb = likesDb;
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") Integer count) {
        return likesDb.mostPopular(count);
    }
}
