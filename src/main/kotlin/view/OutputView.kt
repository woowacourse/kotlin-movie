package view

import model.payment.Money
import model.payment.MoviePaymentResult
import model.payment.Point
import model.reservation.MovieReservationGroup
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
        val grouped = seatGroup.groupBy { it.position.row }.toSortedMap()
        val columns = seatGroup.map { it.position.column }.distinct().sorted()
        println("좌석 배치도")
        print("   ")
        columns.forEach { print(" $it ") }
        println()
        grouped.forEach { (row, rowSeats) ->
            print(" $row ")
            rowSeats.sortedBy { it.position.column }.forEach { seat ->
                print("[${seat.grade.name}]")
            }
            println()
        }
    }

    fun showMovieReservationResult(
        initialMessage: String,
        reservationGroup: MovieReservationGroup,
    ) {
        println(initialMessage)
        reservationGroup
            .groupBy { it.movie.name to it.startTime }
            .forEach { (key, results) ->
                val (movieName, startTime) = key
                val seats = results.joinToString(", ") { "${it.seat.position.row}${it.seat.position.column}" }
                println("- [$movieName] ${startTime.format("yyyy-MM-dd HH:mm")} 좌석: $seats")
            }
    }

    fun totalReservation(
        movieReservationGroup: MovieReservationGroup,
        price: Money,
        point: Point,
    ) {
        println("예매 완료")
        println("내역:")
        movieReservationGroup
            .groupBy { it.movie.name to it.startTime }
            .forEach { (key, results) ->
                val (movieName, startTime) = key
                val seats = results.joinToString(", ") { "${it.seat.position.row}${it.seat.position.column}" }
                println("- [$movieName] ${startTime.format("yyyy-MM-dd HH:mm")} 좌석: $seats")
            }
        println("결제 금액: %,d원 (포인트: %,d원 사용)".format(price.toInt(), point.toInt()))
    }

    fun printTotalPrice(moviePaymentResult: MoviePaymentResult) {
        println("가격 계산")
        print("최종 결제 금액: ")
        println("${"%,d".format(moviePaymentResult.finalPrice.toInt())}원")
    }

    fun end() {
        println("감사합니다.")
    }
}
