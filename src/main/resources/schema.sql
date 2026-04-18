CREATE TABLE IF NOT EXISTS movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    running_time_minutes BIGINT NOT NULL,
    showing_period_start DATE NOT NULL,
    showing_period_end DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS screenings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    start_date_time TIMESTAMP NOT NULL,
    screen_name VARCHAR(50) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS reservation_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    screening_id BIGINT NOT NULL,
    seat_row CHAR(1) NOT NULL,
    seat_column INT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    FOREIGN KEY (screening_id) REFERENCES screenings(id),
    UNIQUE (screening_id, seat_row, seat_column)
)
