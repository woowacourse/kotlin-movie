package view

import model.schedule.MovieSchedule
import model.seat.SeatGroup

object OutputView {
    fun showMovieSchedule(movieSchedule: MovieSchedule) {
        val timeTable = movieSchedule.map { it.screenTime }.sortedBy { it.start }
        timeTable.forEachIndexed { index, time ->
            println("[${index + 1}] ${time.start.format("HH:mm")}")
        }
    }

    fun showSeatGroup(seatGroup: SeatGroup) {
        val grouped = seatGroup.groupBy { it.row }.toSortedMap()
        val columns = seatGroup.map { it.column }.distinct().sorted()
        println("좌석 배치도")
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
}
