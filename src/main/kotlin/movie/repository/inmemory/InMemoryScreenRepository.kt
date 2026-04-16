package movie.repository.inmemory

import movie.model.Screen
import movie.model.seat.Seat
import movie.model.seat.SeatGrade
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import movie.repository.ScreenRepository

object InMemoryScreenRepository : ScreenRepository {
    val screen1 = Screen("1관", defaultSeats())
    val screen2 = Screen("2관", defaultSeats())
    val screen3 = Screen("3관", defaultSeats())

    private val screens: List<Screen> = listOf(screen1, screen2, screen3)

    override fun findByName(name: String): Screen? = screens.find { it.screenName == name }

    private fun defaultSeats(): Seats =
        Seats(
            ('A'..'B').flatMap { row ->
                (1..4).map { col ->
                    Seat(
                        SeatNumber(row, col),
                        SeatGrade.B,
                    )
                }
            } +
                ('C'..'D').flatMap { row ->
                    (1..4).map { col ->
                        Seat(
                            SeatNumber(row, col),
                            SeatGrade.S,
                        )
                    }
                } +
                ('E'..'E').flatMap { row ->
                    (1..4).map { col ->
                        Seat(
                            SeatNumber(row, col),
                            SeatGrade.A,
                        )
                    }
                },
        )
}
