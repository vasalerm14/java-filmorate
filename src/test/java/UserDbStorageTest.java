import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {UserDbStorage.class, UserDbStorageTest.TestConfig.class})
class UserDbStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void testFindUserById() {
        String userEmail = "test@mail.ru";
        String nickName = "test123";
        String name = "Vasilii";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User newUser = new User();
        newUser.setEmail(userEmail);
        newUser.setLogin(nickName);
        newUser.setName(name);
        newUser.setBirthday(birthday);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        User savedUser = userStorage.getUser(1);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        String userEmail = "test@mail.ru";
        String nickName = "test123";
        String name = "Vasilii";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User newUser = new User();
        newUser.setEmail(userEmail);
        newUser.setLogin(nickName);
        newUser.setName(name);
        newUser.setBirthday(birthday);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User createdUser = userStorage.create(newUser);
        createdUser.setName("Vanya");
        User testUser = userStorage.update(createdUser);
        assertThat(testUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(createdUser);
    }

    @Test
    public void testGetAllUsers() {
        String userEmail = "test@mail.ru";
        String nickName = "test123";
        String name = "Vasilii";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User newUser = new User();
        newUser.setEmail(userEmail);
        newUser.setLogin(nickName);
        newUser.setName(name);
        newUser.setBirthday(birthday);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(newUser);
        assertThat(userStorage.getAllUsers().size())
                .usingRecursiveComparison()
                .isEqualTo(2);
    }

    @Configuration
    static class TestConfig {
    }
}
