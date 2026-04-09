package movie.data

import movie.domain.amount.Point
import movie.domain.movie.Movie
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seats
import movie.domain.user.User
import java.time.LocalDate
import java.time.LocalTime

object TheaterData {

    fun createMovies(): List<Movie> {
        val screen = _root_ide_package_.movie.domain.screening.Screen(
            1,
            Seats.createDefault()
        )

        return listOf(
            createF1Movie(screen),
            createToyStory(screen),
            createIronMan(screen),
        )
    }

    private fun createF1Movie(screen: Screen): Movie {
        val screenings = listOf(
            Screening(
                "F1 더 무비", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(10, 20),
                    LocalTime.of(12, 20)
                ), ReservatedSeats(emptyList())
            ),

            Screening(
                "F1 더 무비", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(13, 0),
                    LocalTime.of(15, 0)
                ), ReservatedSeats(emptyList())
            ),

            Screening(
                "F1 더 무비", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(15, 40),
                    LocalTime.of(17, 40)
                ), ReservatedSeats(emptyList())
            ),

            Screening(
                "F1 더 무비", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(20, 10),
                    LocalTime.of(22, 10)
                ), ReservatedSeats(emptyList())
            ),
        )
        return _root_ide_package_.movie.domain.movie.Movie(
            title = "F1 더 무비",
            screenings = _root_ide_package_.movie.domain.screening.Screenings(screenings)
        )
    }

    private fun createToyStory(screen: Screen): Movie {
        val screenings = listOf(
            Screening(
                "토이 스토리", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(13, 30),
                    LocalTime.of(15, 30)
                ), ReservatedSeats(emptyList())
            ),

            Screening(
                "토이 스토리", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(16, 0),
                    LocalTime.of(18, 0)
                ), ReservatedSeats(emptyList())
            ),
        )
        return _root_ide_package_.movie.domain.movie.Movie(
            title = "토이 스토리",
            screenings = _root_ide_package_.movie.domain.screening.Screenings(screenings)
        )
    }

    private fun createIronMan(screen: Screen): Movie {
        val screenings = listOf(
            Screening(
                "아이언맨", screen, ScreeningDateTime(
                    LocalDate.of(2025, 9, 20),
                    LocalTime.of(9, 50),
                    LocalTime.of(11, 50)
                ), ReservatedSeats(emptyList())
            ),
        )
        return _root_ide_package_.movie.domain.movie.Movie(
            title = "아이언맨",
            screenings = _root_ide_package_.movie.domain.screening.Screenings(screenings)
        )
    }

    fun createUser(): User {
        return _root_ide_package_.movie.domain.user.User(
            _root_ide_package_.movie.domain.reservation.Reservations(
                emptyList()
            ), _root_ide_package_.movie.domain.amount.Point(10000)
        )
    }
}
