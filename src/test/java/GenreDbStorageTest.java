import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDb;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {GenreDb.class, GenreDbStorageTest.TestConfig.class})
public class GenreDbStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testGetGenreById() {
        GenreDb genreDb = new GenreDb(jdbcTemplate);
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Комедия");
        assertThat(genreDb.getGenreById(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @Test
    public void testGetAllGenre() {
        GenreDb genreDb = new GenreDb(jdbcTemplate);
        assertThat(genreDb.getAllGenres().size())
                .isEqualTo(6);
    }


    @Configuration
    static class TestConfig {
    }
}
