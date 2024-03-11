package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
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
}
