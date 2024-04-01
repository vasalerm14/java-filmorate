CREATE TABLE IF NOT EXISTS Genre (
                                     id INT PRIMARY KEY,
                                     name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS MPA (
                                   id INT PRIMARY KEY,
                                   name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Film (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    name VARCHAR(255),
                                    description VARCHAR(255),
                                    releaseDate DATE CHECK (releaseDate >= '1895-12-28'),
                                    duration INT,
                                    mpaRating_id INT,
                                    FOREIGN KEY (mpaRating_id) REFERENCES MPA(id)
);


CREATE TABLE IF NOT EXISTS FilmGenre (
                                         film_id INT,
                                         genre_id INT,
                                         FOREIGN KEY (film_id) REFERENCES Film(id),
                                         FOREIGN KEY (genre_id) REFERENCES Genre(id),
                                         PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS Users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL,
                                     login VARCHAR(255),
                                     name VARCHAR(255) NOT NULL,
                                     birthday DATE NOT NULL
);




CREATE TABLE IF NOT EXISTS Friendship (
                                          user_id INT,
                                          friend_id INT,
                                          FOREIGN KEY (user_id) REFERENCES Users(id),
                                          FOREIGN KEY (friend_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Likes (
                                     user_id INT,
                                     film_id INT,
                                     FOREIGN KEY (user_id) REFERENCES Users(id),
                                     FOREIGN KEY (film_id) REFERENCES Film(id),
                                     PRIMARY KEY (user_id, film_id)
);
