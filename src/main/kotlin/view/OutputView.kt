package view

import model.schedule.MovieSchedule

object OutputView {
    fun showMovieSchedule(movieSchedule: MovieSchedule) {
        val timeTable = movieSchedule.map { it.screenTime }.sortedBy { it.start }
        timeTable.forEachIndexed { index, time ->
            println("[${index + 1}] ${time.start.format("HH:mm")}")
        }
    }
}
