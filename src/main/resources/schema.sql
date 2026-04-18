CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    running_time_minutes INT NOT NULL
);

CREATE TABLE IF NOT EXISTS movie_screening (
    id BIGINT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    screen_start TIMESTAMP NOT NULL,
    screen_end TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_price INT,
    used_point INT,
    payment_method VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS reservation_seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    screening_id BIGINT NOT NULL,
    seat VARCHAR(10) NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id),
    FOREIGN KEY (screening_id) REFERENCES movie_screening(id),
    UNIQUE (screening_id, seat)
);
