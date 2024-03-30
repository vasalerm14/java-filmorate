package ru.yandex.practicum.filmorate.storage.Likes;

import ru.yandex.practicum.filmorate.model.Film;

public interface Likes {
    Film addLike(Integer filmId, Integer userId);

    Film removeLike(Integer id, Integer userId);
}
