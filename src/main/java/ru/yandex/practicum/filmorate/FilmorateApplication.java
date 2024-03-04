package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {
	//Привет, не совсем понятно как по-нормальному проверять что существует пользовтаель с определнным id который поставил Like, чтоб удалить его
	//просто странно проверять что id просто больше 0
	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
