package data

import domain.amount.Point
import domain.movie.Movie
import domain.reservation.Reservations
import domain.screening.Screen
import domain.screening.Screening
import domain.screening.ScreeningDateTime
import domain.screening.Screenings
import domain.seat.ReservatedSeats
import domain.seat.Seats
import domain.user.User
import java.time.LocalDate
import java.time.LocalTime

object TheaterData {

    fun createMovies(): List<Movie> {
        val screen = Screen(1, Seats.createDefault())

        return listOf(
            createF1Movie(screen),
            createToyStory(screen),
            createIronMan(screen),
        )
    }

    private fun createF1Movie(screen: Screen): Movie {
        val screenings = listOf(
            Screening(1, screen, ScreeningDateTime(
                LocalDate.of(2025, 9, 20),
                LocalTime.of(10, 20),
                LocalTime.of(12, 20)
            ), ReservatedSeats(emptyList())),

            Screening(1, screen, ScreeningDateTime(
                LocalDate.of(2025, 9, 20),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0)
            ), ReservatedSeats(emptyList())),

            Screening(1, screen, ScreeningDateTime(
                LocalDate.of(2025, 9, 20),
                LocalTime.of(15, 40),
                LocalTime.of(17, 40)
            ), ReservatedSeats(emptyList())),

            Screening(1, screen, ScreeningDateTime(
                LocalDate.of(2025, 9, 20),
                LocalTime.of(20, 10),
                LocalTime.of(22, 10)
            ), ReservatedSeats(emptyList())),
        )
        return Movie(title = "F1 더 무비", screenings = Screenings(screenings))
    }

    private fun createToyStory(screen: Screen): Movie {
        val screenings = listOf(
            Screening(
                2, screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(13, 30),
                    LocalTime.of(15, 30)
                ), ReservatedSeats(emptyList())
            ),

            Screening(2, screen, ScreeningDateTime(
                LocalDate.of(2025, 9, 20),
                LocalTime.of(16, 0),
                LocalTime.of(18, 0)
            ), ReservatedSeats(emptyList())),
        )
        return Movie(title = "토이 스토리", screenings = Screenings(screenings))
    }

    private fun createIronMan(screen: Screen): Movie {
        val screenings = listOf(
            Screening(3, screen, ScreeningDateTime(
                LocalDate.of(2025, 9, 20),
                LocalTime.of(9, 50),
                LocalTime.of(11, 50)
            ), ReservatedSeats(emptyList())),
        )
        return Movie(title = "아이언맨", screenings = Screenings(screenings))
    }

    fun createUser(): User {
        return User(Reservations(emptyList()), Point(10000))
    }
}
