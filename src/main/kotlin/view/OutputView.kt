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

    fun showMovieSeatGroup(screening: MovieScreening) {
        val seatGroup = screening.seatGroup
        val grouped = seatGroup.groupBy { it.row }.toSortedMap()
        val columns = seatGroup.map { it.column }.distinct().sorted()
        println(Message.SEAT_GROUP)
        print("   ")
        columns.forEach { print(" $it ") }
        println()
        grouped.forEach { (row, rowSeats) ->
            print(" $row ")
            rowSeats.sortedBy { it.column }.forEach { seat ->
                print("[${seat.grade.name}]")
            }
            println()
        }
    }

    fun end() {
        println("감사합니다.")
    }
}
