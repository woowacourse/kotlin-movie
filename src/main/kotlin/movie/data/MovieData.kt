package movie.data

import movie.domain.amount.Point
import movie.domain.movie.Movie
import movie.domain.movie.MovieTitle
import movie.domain.movie.Movies
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.ScreenId
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.ScreeningSlot
import movie.domain.screening.Screenings
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
import movie.domain.seat.Seats
import movie.domain.user.User
import java.time.LocalDate
import java.time.LocalTime

object MovieData {
    fun createMovies(): Movies {
        val screen =
            Screen(
                ScreenId(1),
                Seats.createDefault(),
            )

        return Movies(listOf(
            createF1Movie(screen),
            createToyStory(screen),
            createIronMan(screen),
        ))
    }

    private fun createF1Movie(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    MovieTitle("F1 더 무비"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(10, 20),
                            LocalTime.of(12, 20),
                        ),
                    ),
                    ReservatedSeats(emptyList()),
                ),
                Screening(
                    MovieTitle("F1 더 무비"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(13, 0),
                            LocalTime.of(15, 0),
                        ),
                    ),
                    ReservatedSeats(
                        listOf(
                            Seat(SeatRow("B"), SeatColumn(2), SeatGrade.B),
                            Seat(SeatRow("B"), SeatColumn(3), SeatGrade.B),
                            Seat(SeatRow("C"), SeatColumn(3), SeatGrade.S),
                            Seat(SeatRow("E"), SeatColumn(4), SeatGrade.A),
                        ),
                    ),
                ),
                Screening(
                    MovieTitle("F1 더 무비"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(15, 40),
                            LocalTime.of(17, 40),
                        ),
                    ),
                    ReservatedSeats(emptyList()),
                ),
                Screening(
                    MovieTitle("F1 더 무비"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(20, 10),
                            LocalTime.of(22, 10),
                        ),
                    ),
                    ReservatedSeats(emptyList()),
                ),
            )
        return Movie(
            title = MovieTitle("F1 더 무비"),
            screenings = Screenings(screenings),
        )
    }

    private fun createToyStory(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    MovieTitle("토이 스토리"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(13, 30),
                            LocalTime.of(15, 30),
                        ),
                    ),
                    ReservatedSeats(emptyList()),
                ),
                Screening(
                    MovieTitle("토이 스토리"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(16, 0),
                            LocalTime.of(18, 0),
                        ),
                    ),
                    ReservatedSeats(emptyList()),
                ),
            )
        return Movie(
            title =  MovieTitle("토이 스토리"),
            screenings = Screenings(screenings),
        )
    }

    private fun createIronMan(screen: Screen): Movie {
        val screenings =
            listOf(
                Screening(
                    MovieTitle("아이언맨"),
                    ScreeningSlot(
                        screen,
                        ScreeningDateTime(
                            LocalDate.of(2025, 9, 20),
                            LocalTime.of(9, 50),
                            LocalTime.of(11, 50),
                        ),
                    ),
                    ReservatedSeats(emptyList()),
                ),
            )
        return Movie(
            title =  MovieTitle("아이언맨"),
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
