package model

import data.MovieRepository.F1_THE_MOVIE
import data.MovieRepository.IRON_MAN
import data.MovieRepository.ROBOT_DREAM
import data.MovieRepository.TOY_STORY
import model.movie.Movie
import model.movie.Movies
import model.screening.Screening
import model.screening.ScreeningSeatMap
import model.screening.Screenings
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Scheduler {
    private var screenings = Screenings(createScreenings())

    fun getScreenings(
        movie: Movie,
        date: LocalDate,
    ): Screenings = screenings.findBy(movie, date)

    fun getMovies(): Movies = Movies(listOf(F1_THE_MOVIE, TOY_STORY, IRON_MAN, ROBOT_DREAM))

    fun update(newScreening: Screening) {
        screenings = screenings.update(newScreening)
    }

    companion object {
        private val BASE_DATE: LocalDate = LocalDate.of(2025, 9, 20)

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

        private fun createScreenings(): List<Screening> {
            val screen1 = Screen("1관", defaultSeats())
            val screen2 = Screen("2관", defaultSeats())
            val screen3 = Screen("3관", defaultSeats())

            return listOf(
                Screening(
                    F1_THE_MOVIE,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(10, 20)),
                    ScreeningSeatMap(screen1),
                ),
                Screening(
                    F1_THE_MOVIE,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(13, 0)),
                    ScreeningSeatMap(screen1),
                ),
                Screening(
                    F1_THE_MOVIE,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(15, 40)),
                    ScreeningSeatMap(screen1),
                ),
                Screening(
                    F1_THE_MOVIE,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(20, 10)),
                    ScreeningSeatMap(screen1),
                ),
                Screening(
                    TOY_STORY,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(13, 30)),
                    ScreeningSeatMap(screen2),
                ),
                Screening(
                    TOY_STORY,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(16, 0)),
                    ScreeningSeatMap(screen2),
                ),
                Screening(
                    IRON_MAN,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(9, 50)),
                    ScreeningSeatMap(screen3),
                ),
                Screening(
                    ROBOT_DREAM,
                    LocalDateTime.of(BASE_DATE, LocalTime.of(21, 0)),
                    ScreeningSeatMap(screen3),
                ),
            )
        }
    }
}
