package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {
    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer id, Integer userId);

    List<Film> mostPopular(int count);
}
