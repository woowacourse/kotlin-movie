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
import java.time.LocalDateTime

object MovieData {
    fun createMovies(): List<Movie> =
        listOf(
            createF1Movie(
                Screen(
                    1,
                    SeatsData.seats,
                ),
            ),
            createToyStory(
                Screen(
                    2,
                    SeatsData.seats,
                ),
            ),
            createIronMan(
                Screen(
                    3,
                    SeatsData.seats,
                ),
            ),
        )

    private fun createF1Movie(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    1L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 10, 20),
                        LocalDateTime.of(2025, 9, 20, 12, 20),
                    ),
                    ReservedSeats(Seats(emptySet())),
                ),
                Screening(
                    2L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 13, 0),
                        LocalDateTime.of(2025, 9, 20, 15, 0),
                    ),
                    ReservedSeats(
                        Seats(
                            setOf(
                                Seat("B", 2, SeatGrade.B),
                                Seat("B", 3, SeatGrade.B),
                                Seat("C", 3, SeatGrade.S),
                                Seat("E", 4, SeatGrade.A),
                            ),
                        ),
                    ),
                ),
                Screening(
                    3L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 15, 40),
                        LocalDateTime.of(2025, 9, 20, 17, 40),
                    ),
                    ReservedSeats(Seats(emptySet())),
                ),
                Screening(
                    4L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 20, 10),
                        LocalDateTime.of(2025, 9, 20, 22, 10),
                    ),
                    ReservedSeats(Seats(emptySet())),
                ),
            )
        return Movie(
            id = 1,
            title = "F1 더 무비",
            screenings = Screenings(screenings),
        )
    }

    private fun createToyStory(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    5L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 13, 30),
                        LocalDateTime.of(2025, 9, 20, 15, 30),
                    ),
                    ReservedSeats(Seats(emptySet())),
                ),
                Screening(
                    6L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 16, 0),
                        LocalDateTime.of(2025, 9, 20, 18, 0),
                    ),
                    ReservedSeats(Seats(emptySet())),
                ),
            )
        return Movie(
            id = 2,
            title = "토이 스토리",
            screenings = Screenings(screenings),
        )
    }

    private fun createIronMan(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    7L,
                    screen,
                    ScreeningDateTime(
                        LocalDateTime.of(2025, 9, 20, 9, 50),
                        LocalDateTime.of(2025, 9, 20, 11, 50),
                    ),
                    ReservedSeats(Seats(emptySet())),
                ),
            )
        return Movie(
            id = 3,
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
