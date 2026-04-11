package view

import model.MovieReservationResult
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

    fun showReservationInfo(successResults: List<MovieReservationResult.Success>) {
        println(Message.SHOW_RESERVATION_INFO)
        successResults
            .groupBy { it.movie.name to it.screenTime.start }
            .forEach { (key, results) ->
                val (movieName, startTime) = key
                val seats = results.joinToString(", ") { "${it.seat.row}${it.seat.column}" }
                println("- [$movieName] ${startTime.format("yyyy-MM-dd HH:mm")} 좌석: $seats")
            }
    }

    fun end() {
        println("감사합니다.")
    }
}
