//package ru.yandex.practicum.filmorate.controller;
//
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class UserControllerTest {
//
//    static UserController userController = new UserController();
//
//    @Test
//    void validateUserOk() {
//        User validUser = new User();
//        validUser.setName("Test");
//        validUser.setLogin("test");
//        validUser.setEmail("test@yandex.ru");
//        validUser.setBirthday(LocalDate.now().minusDays(1));
//        userController.validation(validUser);
//    }
//
//    @Test
//    void notValidUserEmail1() {
//        User notValidUser = new User();
//        notValidUser.setName("Test");
//        notValidUser.setLogin("test");
//        notValidUser.setEmail("testyandex.ru");
//        notValidUser.setBirthday(LocalDate.now().minusDays(1));
//        assertThrows(ValidationException.class, () -> {
//            userController.validation(notValidUser);
//        });
//    }
//
//    @Test
//    void notValidUserEmail2() {
//        User notValidUser = new User();
//        notValidUser.setName("Test");
//        notValidUser.setLogin("test");
//        notValidUser.setEmail("");
//        notValidUser.setBirthday(LocalDate.now().minusDays(1));
//        assertThrows(ValidationException.class, () -> {
//            userController.validation(notValidUser);
//        });
//    }
//
//    @Test
//    void notvalidUserLogin1() {
//        User notValidUser = new User();
//        notValidUser.setName("Test");
//        notValidUser.setLogin("");
//        notValidUser.setEmail("test@yandex.ru");
//        notValidUser.setBirthday(LocalDate.now().minusDays(1));
//        assertThrows(ValidationException.class, () -> {
//            userController.validation(notValidUser);
//        });
//    }
//
//    @Test
//    void notvalidUserLogin2() {
//        User notValidUser = new User();
//        notValidUser.setName("Test");
//        notValidUser.setLogin("test test");
//        notValidUser.setEmail("test@yandex.ru");
//        notValidUser.setBirthday(LocalDate.now().minusDays(1));
//        assertThrows(ValidationException.class, () -> {
//            userController.validation(notValidUser);
//        });
//    }
//
//    @Test
//    void notvalidUserBirthDay() {
//        User notValidUser = new User();
//        notValidUser.setName("Test");
//        notValidUser.setLogin("test");
//        notValidUser.setEmail("test@yandex.ru");
//        notValidUser.setBirthday(LocalDate.now().plusDays(1));
//        assertThrows(ValidationException.class, () -> {
//            userController.validation(notValidUser);
//        });
//    }
//
//}
