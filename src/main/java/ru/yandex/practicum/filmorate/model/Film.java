package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;
import java.util.LinkedHashSet;

@Data
public class Film {
    private Integer id;

    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1, max = 200)
    private String description;

    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    private Set<Integer> likes = new HashSet<>();
    private final LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private MPA mpa;

    public void setGenres(Collection<Genre> newGenres) {
        this.genres.clear();
        this.genres.addAll(newGenres);
    }
}
