package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    static FilmController filmController = new FilmController();

    @Test
    void validateFilmOk() {
        Film validFilm = new Film();
        validFilm.setName("Test");
        validFilm.setDescription("Test");
        validFilm.setReleaseDate(LocalDate.now());
        validFilm.setDuration(100);
        filmController.validation(validFilm);
    }

    @Test
    void validateFilmNullName() {
        Film notValidFilm = new Film();
        notValidFilm.setDescription("Test");
        notValidFilm.setReleaseDate(LocalDate.now());
        notValidFilm.setDuration(100);
        notValidFilm.setName(null);
        assertThrows(ValidationException.class, () -> {
            filmController.validation(notValidFilm);
        });
    }

    @Test
    void validateFilm201Symbol() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Test");
        notValidFilm.setDescription("UmyHCJcKUhgrbLOKoohGlHsqaPWQVDOxHHyhkinfKOmfGPtmZWXdrrojQJNQxrsSrJDZjkyzNMlGvIZCWdigLVqVwcxUXAOTEnbSfVYQeVFBJyFnekLkZIhgtRcVSOLFtWYnZTVvRSrFtcpdVBOpAVaCexdbawZTltiokEYWPTICebgsWAZwzCMGswCoMYrUYQRVQsMQI");
        notValidFilm.setReleaseDate(LocalDate.now());
        notValidFilm.setDuration(100);
        assertThrows(ValidationException.class, () -> {
            filmController.validation(notValidFilm);
        });
    }

    @Test
    void validateFilmBefore28121895() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Test");
        notValidFilm.setDescription("Test");
        notValidFilm.setReleaseDate(LocalDate.of(1894, 12, 28));
        notValidFilm.setDuration(100);
        assertThrows(ValidationException.class, () -> {
            filmController.validation(notValidFilm);
        });
    }

    @Test
    void validateFilmNegativeDuration() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Test");
        notValidFilm.setDescription("Test");
        notValidFilm.setReleaseDate(LocalDate.now());
        notValidFilm.setDuration(-1);
        assertThrows(ValidationException.class, () -> {
            filmController.validation(notValidFilm);
        });
    }
}
