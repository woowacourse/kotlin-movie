CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    running_time_minutes INT NOT NULL
);

CREATE TABLE IF NOT EXISTS screen (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS screening (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    screen_id BIGINT NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id),
    FOREIGN KEY (screen_id) REFERENCES screen(id)
);

CREATE TABLE IF NOT EXISTS reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    used_points INT NOT NULL DEFAULT 0,
    payment_method VARCHAR(20) NOT NULL,
    total_price INT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    screening_id BIGINT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id),
    FOREIGN KEY (screening_id) REFERENCES screening(id)
);

CREATE TABLE IF NOT EXISTS reservation_seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_item_id BIGINT NOT NULL,
    seat_row VARCHAR(1) NOT NULL,
    seat_column INT NOT NULL,
    FOREIGN KEY (reservation_item_id) REFERENCES reservation_item(id)
);

CREATE TABLE IF NOT EXISTS reserved_seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    screening_id BIGINT NOT NULL,
    seat_row VARCHAR(1) NOT NULL,
    seat_column INT NOT NULL,
    FOREIGN KEY (screening_id) REFERENCES screening(id),
    UNIQUE (screening_id, seat_row, seat_column)
);
