package persistence.seed

import domain.Id
import domain.cinema.Movie
import domain.cinema.MovieTheater
import domain.cinema.Screen
import domain.cinema.ScreeningSchedule
import kotlinx.datetime.LocalDateTime

internal object CinemaSeedData {
    fun movieTheater(): MovieTheater {
        val movies =
            listOf(
                Movie("F1 더 무비", Id("movie-f1"), 130),
                Movie("토이 스토리", Id("movie-toy-story"), 100),
                Movie("아이언맨", Id("movie-iron-man"), 126),
            )

        val screens =
            listOf(
                Screen(FixedSeatLayout.createSeats(), Id("screen-1")),
                Screen(FixedSeatLayout.createSeats(), Id("screen-2")),
                Screen(FixedSeatLayout.createSeats(), Id("screen-3")),
            )

        val screenings =
            listOf(
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 10, 20), screens[0], movies[0]),
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 13, 0), screens[0], movies[0]),
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 15, 40), screens[0], movies[0]),
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 20, 10), screens[0], movies[0]),
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 13, 30), screens[1], movies[1]),
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 16, 0), screens[1], movies[1]),
                ScreeningSchedule(LocalDateTime(2025, 9, 20, 9, 50), screens[2], movies[2]),
            )

        return MovieTheater(
            screens = screens,
            movies = movies,
            screenings = screenings,
        )
    }
}
