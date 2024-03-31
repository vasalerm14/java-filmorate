package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaDb;

import java.util.Collection;

@RestController
public class MpaController {
    private final MpaDb mpaDao;

    @Autowired
    public MpaController(MpaDb mpaDao) {
        this.mpaDao = mpaDao;
    }

    @GetMapping("/mpa")
    public Collection<MPA> getMpa() {
        return mpaDao.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMpaById(@PathVariable int id) {
        return mpaDao.getMPAById(id);
    }
}
