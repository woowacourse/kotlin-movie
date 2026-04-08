package domain.screening

import domain.movie.Movie
import domain.movie.Movies
import domain.movie.RunningTime
import domain.movie.ShowingPeriod
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.SeatNumber
import domain.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Scheduler {
    private val screens: Screens = Screens(createScreens())

    fun getScreenings(movie: Movie, date: LocalDate): Screenings =
        screens.findScreenings(movie, date)

    fun getMovies(): Movies = Movies(listOf(F1_THE_MOVIE, TOY_STORY, IRON_MAN))

    companion object {
        private val F1_THE_MOVIE = Movie(
            title = "F1 더 무비",
            runningTime = RunningTime(160),
            showingPeriod = ShowingPeriod(
                startDate = LocalDate.of(2025, 9, 1),
                endDate = LocalDate.of(2025, 9, 30)
            )
        )

        private val TOY_STORY = Movie(
            title = "토이스토리",
            runningTime = RunningTime(150),
            showingPeriod = ShowingPeriod(
                startDate = LocalDate.of(2025, 9, 1),
                endDate = LocalDate.of(2025, 9, 30)
            )
        )

        private val IRON_MAN = Movie(
            title = "아이언맨",
            runningTime = RunningTime(130),
            showingPeriod = ShowingPeriod(
                startDate = LocalDate.of(2025, 9, 1),
                endDate = LocalDate.of(2025, 9, 30)
            )
        )

        private val BASE_DATE: LocalDate = LocalDate.of(2025, 9, 20)

        private fun defaultSeats(): Seats = Seats(
            ('A'..'B').flatMap { row ->
                (1..4).map { col ->
                    Seat(
                        SeatNumber(row, col),
                        SeatGrade.B
                    )
                }
            } +
                    ('C'..'D').flatMap { row ->
                        (1..4).map { col ->
                            Seat(
                                SeatNumber(row, col),
                                SeatGrade.S
                            )
                        }
                    } +
                    ('E'..'E').flatMap { row ->
                        (1..4).map { col ->
                            Seat(
                                SeatNumber(row, col),
                                SeatGrade.A
                            )
                        }
                    }
        )

        private fun createScreens(): List<Screen> {
            val seats1 = defaultSeats()
            val seats2 = defaultSeats()
            val seats3 = defaultSeats()

            return listOf(
                Screen(
                    screenName = "1관",
                    seats = seats1,
                    screenings = Screenings(
                        listOf(
                            Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(10, 20)), seats1),
                            Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(13, 0)), seats1),
                            Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(15, 40)), seats1),
                            Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(20, 10)), seats1)
                        )
                    )
                ),
                Screen(
                    screenName = "2관",
                    seats = seats2,
                    screenings = Screenings(
                        listOf(
                            Screening(TOY_STORY, LocalDateTime.of(BASE_DATE, LocalTime.of(13, 30)), seats2),
                            Screening(TOY_STORY, LocalDateTime.of(BASE_DATE, LocalTime.of(16, 0)), seats2)
                        )
                    )
                ),
                Screen(
                    screenName = "3관",
                    seats = seats3,
                    screenings = Screenings(
                        listOf(
                            Screening(IRON_MAN, LocalDateTime.of(BASE_DATE, LocalTime.of(9, 50)), seats3)
                        )
                    )
                )
            )
        }
    }
}
