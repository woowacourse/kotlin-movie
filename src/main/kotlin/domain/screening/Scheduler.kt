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
    private val screenings = Screenings(createScreenings())

    fun getScreenings(movie: Movie, date: LocalDate): Screenings =
        screenings.findBy(movie, date)

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

        private fun createScreenings(): List<Screening> {
            val screen1 = Screen("1관", defaultSeats())
            val screen2 = Screen("2관", defaultSeats())
            val screen3 = Screen("3관", defaultSeats())

            return listOf(
                Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(10, 20)), screen1),
                Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(13, 0)), screen1),
                Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(15, 40)), screen1),
                Screening(F1_THE_MOVIE, LocalDateTime.of(BASE_DATE, LocalTime.of(20, 10)), screen1),
                Screening(TOY_STORY, LocalDateTime.of(BASE_DATE, LocalTime.of(13, 30)), screen2),
                Screening(TOY_STORY, LocalDateTime.of(BASE_DATE, LocalTime.of(16, 0)), screen2),
                Screening(IRON_MAN, LocalDateTime.of(BASE_DATE, LocalTime.of(9, 50)), screen3)
            )
        }
    }
}
