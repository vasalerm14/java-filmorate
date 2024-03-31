package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MpaStorage {
    MPA getMPAById(Integer id);
    Collection<MPA> getAllMpa();
}
