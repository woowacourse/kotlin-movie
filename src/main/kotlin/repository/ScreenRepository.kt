package repository

import model.Screen
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats

object ScreenRepository {
    val screen1 = Screen("1관", defaultSeats())
    val screen2 = Screen("2관", defaultSeats())
    val screen3 = Screen("3관", defaultSeats())

    private fun defaultSeats(): Seats = Seats(
        ('A'..'B').flatMap { row ->
            (1..4).map { col ->
                Seat(
                    SeatNumber(row, col),
                    SeatGrade.B,
                )
            }
        } + ('C'..'D').flatMap { row ->
            (1..4).map { col ->
                Seat(
                    SeatNumber(row, col),
                    SeatGrade.S,
                )
            }
        } + ('E'..'E').flatMap { row ->
            (1..4).map { col ->
                Seat(
                    SeatNumber(row, col),
                    SeatGrade.A,
                )
            }
        },
    )
}
