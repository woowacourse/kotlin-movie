package movie.data.db

import java.sql.Connection

object SchemaInitializer {
    fun initialize(connection: Connection) {
        connection.createStatement().use { statement ->
            statement.execute(
                """
                create table if not exists movies (
                    id bigint primary key,
                    title varchar(255) not null,
                    running_time_minutes int not null
                );
                """.trimIndent(),
            )

            statement.execute(
                """
                create table if not exists screenings (
                    id bigint primary key,
                    movie_id bigint not null,
                    screen_id int not null,
                    start_at timestamp not null,
                    end_at timestamp not null,
                    foreign key (movie_id) references movies(id)
                );
                """.trimIndent(),
            )

            statement.execute(
                """
                create table if not exists reservation_orders (
                    id bigint auto_increment primary key,
                    used_points int not null,
                    payment_method varchar(30) not null,
                    total_price int not null
                );
                """.trimIndent(),
            )

            statement.execute(
                """
                create table if not exists reservations (
                    id bigint auto_increment primary key,
                    reservation_order_id bigint not null,
                    screening_id bigint not null,
                    foreign key (reservation_order_id) references reservation_orders(id),
                    foreign key (screening_id) references screenings(id)
                );
                """.trimIndent(),
            )

            statement.execute(
                """
                create table if not exists reservation_seats (
                    id bigint auto_increment primary key,
                    reservation_id bigint not null,
                    seat_row varchar(1) not null,
                    seat_column int not null,
                    seat_grade varchar(1) not null,
                    foreign key (reservation_id) references reservations(id),
                    unique (reservation_id, seat_row, seat_column)
                );
                """.trimIndent(),
            )
        }
    }
}
