CREATE TABLE IF NOT EXISTS movies (
    id VARCHAR(36) PRIMARY KEY,
    movie_id VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255),
    age_rating VARCHAR(10),
    run_time INT,
    rating DOUBLE,
    release_date DATE,
    tagline TEXT,
    synopsis TEXT,
    poster VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genres (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(100) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS movie_genres (
    movie_id VARCHAR(36) NOT NULL,
    genre_id INT NOT NULL,
    PRIMARY KEY (movie_id, genre_id),
    CONSTRAINT fk_mg_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    CONSTRAINT fk_mg_genre FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS actors (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(255) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS movie_actors (
    movie_id VARCHAR(36) NOT NULL,
    actor_id INT NOT NULL,
    PRIMARY KEY (movie_id, actor_id),
    CONSTRAINT fk_ma_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    CONSTRAINT fk_ma_actor FOREIGN KEY (actor_id) REFERENCES actors(id) ON DELETE CASCADE
    );