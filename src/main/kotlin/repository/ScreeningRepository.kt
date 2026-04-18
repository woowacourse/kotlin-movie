package repository

import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.ScreeningPeriod
import domain.movie.Title
import domain.screening.Screening
import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.screening.ScreeningSchedule
import domain.screening.TimeRange
import domain.seat.Column
import domain.seat.ReserveState
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatPosition
import domain.seat.Seats
import java.sql.Connection

class ScreeningRepository(
    private val connection: Connection,
) {
    fun getSchedule(): ScreeningSchedule {
        val reservedSeats = findReservedSeatsByScreening()
        val roomSeats = findAllRoomSeats()

        val screenings = mutableListOf<Screening>()
        connection.createStatement().use { stmt ->
            val rs =
                stmt.executeQuery(
                    """
                    SELECT screening.id, screening.start_time,
                        movie.title, movie.running_time, movie.period_start, movie.period_end,
                        room.id as room_id, room.name, room.operating_start, room.operating_end
                    FROM SCREENING screening
                    JOIN MOVIE movie ON screening.movie_id = movie.id
                    JOIN SCREENING_ROOM room ON screening.room_id = room.id
                    """.trimIndent(),
                )
            while (rs.next()) {
                val screeningId = rs.getInt("id")
                val roomId = rs.getInt("room_id")
                val reserved = reservedSeats[screeningId] ?: emptySet()
                val seats =
                    (roomSeats[roomId] ?: emptyList()).map { position ->
                        val state =
                            if (position in reserved) ReserveState.RESERVED else ReserveState.AVAILABLE
                        Seat(position, state)
                    }

                screenings.add(
                    Screening(
                        id = screeningId.toString(),
                        movie =
                            Movie(
                                title = Title(rs.getString("title")),
                                runningTime = RunningTime(rs.getInt("running_time")),
                                screeningPeriod =
                                    ScreeningPeriod(
                                        startDate =
                                            rs
                                                .getTimestamp("period_start")
                                                .toLocalDateTime()
                                                .toLocalDate(),
                                        endDate =
                                            rs
                                                .getTimestamp("period_end")
                                                .toLocalDateTime()
                                                .toLocalDate(),
                                    ),
                            ),
                        room =
                            ScreeningRoom(
                                name = ScreeningRoomName(rs.getString("name")),
                                operatingTime =
                                    TimeRange(
                                        start = rs.getTime("operating_start").toLocalTime(),
                                        end = rs.getTime("operating_end").toLocalTime(),
                                    ),
                                seats = Seats(seats),
                            ),
                        startTime = rs.getTimestamp("start_time").toLocalDateTime(),
                    ),
                )
            }
        }
        return ScreeningSchedule(screenings)
    }

    private fun findAllRoomSeats(): Map<Int, List<SeatPosition>> {
        val result = mutableMapOf<Int, MutableList<SeatPosition>>()
        connection.createStatement().use { stmt ->
            val rs =
                stmt.executeQuery(
                    """SELECT room_id, "row", col FROM SCREENING_ROOM_SEAT""",
                )
            while (rs.next()) {
                val roomId = rs.getInt("room_id")
                val position =
                    SeatPosition(
                        row = Row.valueOf(rs.getString("row")),
                        column = Column(rs.getInt("col")),
                    )
                result.getOrPut(roomId) { mutableListOf() }.add(position)
            }
        }
        return result
    }

    private fun findReservedSeatsByScreening(): Map<Int, Set<SeatPosition>> {
        val result = mutableMapOf<Int, MutableSet<SeatPosition>>()
        connection.createStatement().use { stmt ->
            val rs =
                stmt.executeQuery(
                    """
                    SELECT reservation.screening_id, seat."row", seat.col
                    FROM RESERVATION_SEAT seat
                    JOIN RESERVATION reservation ON seat.reservation_id = reservation.id
                    """.trimIndent(),
                )
            while (rs.next()) {
                val screeningId = rs.getInt("screening_id")
                val position =
                    SeatPosition(
                        row = Row.valueOf(rs.getString("row")),
                        column = Column(rs.getInt("col")),
                    )
                result.getOrPut(screeningId) { mutableSetOf() }.add(position)
            }
        }
        return result
    }
}
