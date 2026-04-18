DROP TABLE IF EXISTS reserved_seats;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS screenings;
DROP TABLE IF EXISTS screening_rooms;
DROP TABLE IF EXISTS movies;

CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    running_time INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE screening_rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    operating_start_time TIME NOT NULL,
    operating_end_time TIME NOT NULL
);

CREATE TABLE screenings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (room_id) REFERENCES screening_rooms(id)
);

CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    used_points INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    total_price INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reserved_seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    screening_id BIGINT NOT NULL,
    seat_row VARCHAR(5) NOT NULL,
    seat_column INT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    FOREIGN KEY (screening_id) REFERENCES screenings(id)
);

-- 초기 데이터 삽입
INSERT INTO movies (id, title, running_time, start_date, end_date) VALUES (1, '커브볼', 169, '2026-04-10', '2026-09-30');
INSERT INTO movies (id, title, running_time, start_date, end_date) VALUES (2, '하로', 180, '2026-04-01', '2026-09-30');

INSERT INTO screening_rooms (id, name, operating_start_time, operating_end_time) VALUES (1, '1관', '09:00:00', '23:00:00');
INSERT INTO screening_rooms (id, name, operating_start_time, operating_end_time) VALUES (2, '2관', '09:00:00', '23:00:00');

INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (101, 1, 1, '2026-04-20 13:00:00');
INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (102, 1, 2, '2026-04-20 16:00:00');
INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (201, 2, 2, '2026-04-30 9:00:00');
INSERT INTO screenings (id, movie_id, room_id, start_time) VALUES (202, 2, 2, '2026-04-30 9:00:00');
