package view

import model.schedule.MovieScreening

object OutputView {
    fun showInvalidMovieName() {
        println(Message.INVALID_MOVIE_NAME)
    }

    fun showErrorMessage(message: String?) {
        println(message ?: "오류가 발생했습니다.")
    }

    fun showMovieScreenings(screenings: List<MovieScreening>) {
        println(Message.MOVIE_SCREENINGS_LIST)
        screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.screenTime.start}")
        }
    }

    fun end() {
        println("감사합니다.")
    }
}
