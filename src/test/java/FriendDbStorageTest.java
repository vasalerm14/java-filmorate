import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {FriendDbStorage.class, FriendDbStorageTest.TestConfig.class})
public class FriendDbStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddFriend() {
        FriendDbStorage friendDbStorage = new FriendDbStorage(jdbcTemplate);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        String userEmail = "test@mail.ru";
        String nickName = "test123";
        String name = "Vasilii";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User newUser = new User();
        newUser.setEmail(userEmail);
        newUser.setLogin(nickName);
        newUser.setName(name);
        newUser.setBirthday(birthday);
        userDbStorage.create(newUser);
        userDbStorage.create(newUser);
        friendDbStorage.addFriend(1, 2);
        assertThat(friendDbStorage.getAllFriends(1).size())
                .isEqualTo(1);
    }

    @Test
    public void testRemoveFriend() {
        FriendDbStorage friendDbStorage = new FriendDbStorage(jdbcTemplate);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        String userEmail = "test@mail.ru";
        String nickName = "test123";
        String name = "Vasilii";
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User newUser = new User();
        newUser.setEmail(userEmail);
        newUser.setLogin(nickName);
        newUser.setName(name);
        newUser.setBirthday(birthday);
        userDbStorage.create(newUser);
        userDbStorage.create(newUser);
        friendDbStorage.addFriend(1, 2);
        friendDbStorage.removeFriend(1, 2);
        assertThat(friendDbStorage.getAllFriends(1).size())
                .isEqualTo(0);
    }


    @Configuration
    static class TestConfig {
    }
}
