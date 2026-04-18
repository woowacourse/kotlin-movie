CREATE TABLE IF NOT EXISTS movie (
    id           BIGINT       AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    running_time INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS schedule (
    id         BIGINT    AUTO_INCREMENT PRIMARY KEY,
    movie_id   BIGINT    NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE IF NOT EXISTS reservation (
    id           BIGINT    AUTO_INCREMENT PRIMARY KEY,
    schedule_id  BIGINT    NOT NULL,
    total_price  INT       NOT NULL,
    reserved_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (schedule_id) REFERENCES schedule(id)
);

CREATE TABLE IF NOT EXISTS reserved_seat (
    id             BIGINT  AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT  NOT NULL,
    schedule_id    BIGINT  NOT NULL,
    seat_number    VARCHAR(10) NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation(id),
    FOREIGN KEY (schedule_id) REFERENCES schedule(id),
    CONSTRAINT unique_seat_per_schedule UNIQUE (schedule_id, seat_number)
);
