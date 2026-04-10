package movie.data

import movie.domain.amount.Point
import movie.domain.movie.Movie
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import movie.domain.user.User
import java.time.LocalDate
import java.time.LocalTime

object MovieData {
    fun createMovies(): List<Movie> {
        val screen =
            Screen(
                1,
                Seats.createDefault(),
            )

        return listOf(
            createF1Movie(screen),
            createToyStory(screen),
            createIronMan(screen),
        )
    }

    private fun createF1Movie(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    "F1 더 무비",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(10, 20),
                        LocalTime.of(12, 20),
                    ),
                    ReservedSeats(emptySet()),
                ),
                Screening(
                    "F1 더 무비",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(13, 0),
                        LocalTime.of(15, 0),
                    ),
                    ReservedSeats(
                        setOf(
                            Seat("B", 2, SeatGrade.B),
                            Seat("B", 3, SeatGrade.B),
                            Seat("C", 3, SeatGrade.S),
                            Seat("E", 4, SeatGrade.A),
                        ),
                    ),
                ),
                Screening(
                    "F1 더 무비",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(15, 40),
                        LocalTime.of(17, 40),
                    ),
                    ReservedSeats(emptySet()),
                ),
                Screening(
                    "F1 더 무비",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(20, 10),
                        LocalTime.of(22, 10),
                    ),
                    ReservedSeats(emptySet()),
                ),
            )
        return Movie(
            title = "F1 더 무비",
            screenings = Screenings(screenings),
        )
    }

    private fun createToyStory(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    "토이 스토리",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(13, 30),
                        LocalTime.of(15, 30),
                    ),
                    ReservedSeats(emptySet()),
                ),
                Screening(
                    "토이 스토리",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(16, 0),
                        LocalTime.of(18, 0),
                    ),
                    ReservedSeats(emptySet()),
                ),
            )
        return Movie(
            title = "토이 스토리",
            screenings = Screenings(screenings),
        )
    }

    private fun createIronMan(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    "아이언맨",
                    screen,
                    ScreeningDateTime(
                        LocalDate.of(2025, 9, 20),
                        LocalTime.of(9, 50),
                        LocalTime.of(11, 50),
                    ),
                    ReservedSeats(emptySet()),
                ),
            )
        return Movie(
            title = "아이언맨",
            screenings = Screenings(screenings),
        )
    }

    fun createUser(): User =
        User(
            Reservations(
                emptyList(),
            ),
            Point(10000),
        )
}
