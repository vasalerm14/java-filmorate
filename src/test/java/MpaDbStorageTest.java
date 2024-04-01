import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaDb;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {MpaDb.class, MpaDbStorageTest.TestConfig.class})
public class MpaDbStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testGetMPAById() {
        MpaDb mpaDb = new MpaDb(jdbcTemplate);
        MPA mpa = new MPA();
        mpa.setId(1);
        mpa.setName("G");
        assertThat(mpaDb.getMPAById(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpa);
    }

    @Test
    public void testGetAllMpa() {
        MpaDb mpaDb = new MpaDb(jdbcTemplate);
        assertThat(mpaDb.getAllMpa().size())
                .isEqualTo(5);
    }


    @Configuration
    static class TestConfig {
    }
}
