package movie.repository.jdbc

import movie.database.DatabaseConnectionFactory
import movie.model.reservation.Reservation
import movie.model.reservation.Reservations
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import movie.repository.ReservationRepository
import movie.repository.ScreeningRepository
import java.time.LocalDate

class JdbcReservationRepository(
    private val isLocal: Boolean,
    private val screeningRepository: ScreeningRepository,
) : ReservationRepository {
    override fun save(reservation: Reservation) {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            val sql =
                """
                INSERT INTO reservation (screening_id, seats)
                VALUES (?, ?)
                """.trimIndent()

            conn.prepareStatement(sql).use { pStatement ->
                val screeningId =
                    requireNotNull(reservation.screening.id) {
                        "상영 회차 ID가 없습니다."
                    }
                val seatString =
                    reservation.seats.seatNumbers.joinToString(",") {
                        "${it.row}${it.column}"
                    }

                pStatement.setLong(1, screeningId)
                pStatement.setString(2, seatString)

                pStatement.executeUpdate()
            }
        }
    }

    override fun findByDate(date: LocalDate): Reservations {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)
        val reservations = mutableListOf<Reservation>()

        connection.use { conn ->
            val sql =
                """
                SELECT r.seats, s.id AS screening_id, s.start_date_time
                FROM reservation r
                INNER JOIN screening s ON r.screening_id = s.id
                WHERE CAST(s.start_date_time AS DATE) = ?
                """.trimIndent()

            conn.prepareStatement(sql).use { pStatement ->
                pStatement.setObject(1, date)

                val resultSet = pStatement.executeQuery()

                while (resultSet.next()) {
                    val screeningId = resultSet.getLong("screening_id")
                    val seatsString = resultSet.getString("seats")

                    val screening =
                        requireNotNull(screeningRepository.findById(screeningId)) {
                            "DB에 저장된 회차 아이디($screeningId)를 찾을 수 없습니다."
                        }
                    val seatNumbers =
                        seatsString
                            .split(",")
                            .map { it.trim() }
                            .map { SeatNumber(it[0], it.substring(1).toInt()) }

                    val seats =
                        Seats(
                            seatNumbers.map {
                                screening.seatMap.screen.seats
                                    .findSeat(it)
                            },
                        )

                    reservations.add(
                        Reservation(screening, seats),
                    )
                }
            }
        }
        return Reservations(reservations)
    }
}
