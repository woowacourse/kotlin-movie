package view

import model.MovieReservationResult
import model.payment.Money
import model.payment.Point
import model.schedule.MovieSchedule
import model.seat.SeatGroup
import kotlin.collections.component1
import kotlin.collections.component2

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

    fun showReservationInfo(successResults: List<MovieReservationResult.Success>) {
        println("바구니에 추가됨")
        successResults
            .groupBy { it.movie.name to it.screenTime.start }
            .forEach { (key, results) ->
                val (movieName, startTime) = key
                val seats = results.joinToString(", ") { "${it.seat.row}${it.seat.column}" }
                println("- [$movieName] ${startTime.format("yyyy-MM-dd HH:mm")} 좌석: $seats")
            }
    }

    fun showShoppingCart(successResults: List<MovieReservationResult.Success>) {
        println("장바구니")
        successResults
            .groupBy { it.movie.name to it.screenTime.start }
            .forEach { (key, results) ->
                val (movieName, startTime) = key
                val seats = results.joinToString(", ") { "${it.seat.row}${it.seat.column}" }
                println("- [$movieName] ${startTime.format("yyyy-MM-dd HH:mm")} 좌석: $seats")
            }
    }

    fun totalReservation(
        successResults: List<MovieReservationResult.Success>,
        price: Money,
        point: Point,
    ) {
        println("예매 완료")
        println("내역:")
        successResults
            .groupBy { it.movie.name to it.screenTime.start }
            .forEach { (key, results) ->
                val (movieName, startTime) = key
                val seats = results.joinToString(", ") { "${it.seat.row}${it.seat.column}" }
                println("- [$movieName] ${startTime.format("yyyy-MM-dd HH:mm")} 좌석: $seats")
            }
        println("결제 금액: %,d원 (포인트: %,d원 사용)".format(price, point))
    }

    fun printTotalPrice(price: Money) {
        println(Message.TOTAL_PRICE_TITLE)
        print(Message.TOTAL_PRICE)
        println("${"%,d".format(price.toInt())}원")
    }

    fun end() {
        println("감사합니다.")
    }
}
