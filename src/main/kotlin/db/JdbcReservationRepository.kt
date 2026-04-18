package db

import model.reservation.Reservations
import repository.MovieRepository
import repository.ReservationRepository
import repository.ScreeningRepository
import java.sql.Connection
import java.sql.Statement

class JdbcReservationRepository(
    private val connection: Connection,
    private val movieRepository: MovieRepository,
    private val screeningRepository: ScreeningRepository,
) : ReservationRepository {
    override fun save(reservations: Reservations): Long {
        val stmt =
            connection.prepareStatement(
                "INSERT INTO reservations DEFAULT VALUES",
                Statement.RETURN_GENERATED_KEYS,
            )
        stmt.executeUpdate()
        val rs = stmt.generatedKeys
        check(rs.next()) { "예약 ID 생성에 실패했습니다." }
        val reservationId = rs.getLong(1)

        saveReservationSeats(reservationId, reservations)
        return reservationId
    }

    private fun saveReservationSeats(
        reservationId: Long,
        reservations: Reservations,
    ) {
        val seatStmt =
            connection.prepareStatement(
                "INSERT INTO reservation_seats (reservation_id, screening_id, seat_row, seat_column) VALUES (?, ?, ?, ?)",
            )
        for (reservation in reservations) {
            val movieId =
                movieRepository.findIdByTitle(reservation.movieTitle())
                    ?: error("영화를 찾을 수 없습니다: ${reservation.movieTitle()}")
            val screeningId =
                screeningRepository.findId(movieId, reservation.startDateTime())
                    ?: error("상영을 찾을 수 없습니다.")
            for (seatNumber in reservation.seats.seatNumbers()) {
                seatStmt.setLong(1, reservationId)
                seatStmt.setLong(2, screeningId)
                seatStmt.setString(3, seatNumber.row.toString())
                seatStmt.setInt(4, seatNumber.column)
                seatStmt.addBatch()
            }
        }
        seatStmt.executeBatch()
    }
}
