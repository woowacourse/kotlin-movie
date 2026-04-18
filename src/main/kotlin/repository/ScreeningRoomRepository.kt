package repository

import domain.common.TimeRange
import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.seat.*
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class ScreeningRoomRepository(private val dataSource: DataSource) {
    fun findById(id: Long): ScreeningRoom {
        val sql = "SELECT * FROM screening_rooms WHERE id = ?"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { stmt ->
                stmt.setLong(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return ScreeningRoom(
                            id = rs.getLong("id"),
                            name = ScreeningRoomName(rs.getString("name")),
                            operatingTime = TimeRange(
                                rs.getTime("operating_start_time").toLocalTime(),
                                rs.getTime("operating_end_time").toLocalTime()
                            ),
                            seats = createDefaultSeats()
                        )
                    }
                }
            }
        }
        throw IllegalArgumentException("상영관을 찾을 수 없습니다.")
    }

    private fun createDefaultSeats(): Seats {
        val seats = mutableListOf<Seat>()
        val rowNames = listOf("A", "B", "C", "D", "E")

        for (rowName in rowNames) {
            for (col in 1..4) {
                seats.add(Seat(SeatPosition(Row(rowName), Column(col))))
            }
        }
        return Seats(seats)
    }
}
