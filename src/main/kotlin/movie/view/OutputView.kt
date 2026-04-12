package movie.view

import movie.domain.Cart
import movie.domain.Price
import movie.domain.Reservation
import movie.domain.Schedule
import movie.domain.point.Point
import movie.domain.seat.SeatNumber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OutputView {
    fun printErrorMessage(message: String?) {
        println(message ?: "알 수 없는 오류입니다. 다시 시도해주세요.")
    }

    fun printMovieStartTimes(movieTimes: List<LocalDateTime>) {
        println("해당 날짜의 상영 목록")
        movieTimes.forEachIndexed { index, time ->
            val hour = "%02d".format(time.hour)
            val minute = "%02d".format(time.minute)

            println("[${index + 1}] $hour:$minute")
        }
    }

    fun printSeats(schedule: Schedule) {
        val seats = schedule.theater.getSeats()
        println("좌석 배치도")

        val groupedByRow =
            seats
                .groupBy { it.seatNumber.row.toString() }
                .toSortedMap()

        val firstRowSeats =
            groupedByRow.values
                .firstOrNull()
                .orEmpty()
                .sortedBy { it.seatNumber.col.toString() }

        val header =
            buildString {
                append("    ")
                firstRowSeats.forEach { seat ->
                    append(
                        seat.seatNumber.col
                            .toString()
                            .padStart(4),
                    )
                }
            }
        println(header)

        groupedByRow.forEach { (row, rowSeats) ->
            val rowText =
                buildString {
                    append(row)
                    append(" ")
                    rowSeats
                        .sortedBy { it.seatNumber.col.toString() }
                        .forEach { seat ->
                            when (schedule.isReservationSeat(seat.seatNumber)) {
                                true -> append("[ X] ")
                                false -> append("[ ${seat.rank.name}] ")
                            }
                        }
                }
            println(rowText)
        }
    }

    fun printReservationAddMessage(
        schedule: Schedule,
        seats: List<SeatNumber>,
    ) {
        val seatNumbers = seats.joinToString(", ")
        val startTime =
            schedule.startTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            )
        println("장바구니에 추가됨")

        println("- [${schedule.movie.title.value}] $startTime 좌석: $seatNumbers")
    }

    fun printCart(cart: Cart) {
        println("장바구니")

        val reservations = cart.getReservations()
        reservations.forEach {
            printReservations(it)
        }
    }

    fun printTotalPrice(totalPrice: Price) {
        println("가격 계산")
        println("최종 결제 금액: ${formatWithComma(totalPrice.amount)}원")
    }

    fun printReceipt(
        cart: Cart,
        paymentPrice: Price,
        usePoint: Point,
    ) {
        println("예매 완료")
        println("내역:")

        val reservations = cart.getReservations()
        reservations.forEach {
            printReservations(it)
        }

        val usePointString = formatWithComma(usePoint.amount)
        val paymentPriceString = formatWithComma(paymentPrice.amount)

        println("결제 금액: ${paymentPriceString}원  (포인트 ${usePointString}원 사용)")
    }

    fun printThankYou() {
        println("감사합니다.")
    }

    fun formatWithComma(value: Int): String = "%,d".format(value)

    private fun printReservations(reservation: Reservation) {
        val screeningMovie = reservation.schedule
        val hour = "%02d".format(screeningMovie.startTime.hour)
        val minute = "%02d".format((screeningMovie.startTime.minute))
        val movieTime = "$hour:$minute"
        val date = screeningMovie.startTime.toLocalDate()
        val seatNumbers = reservation.seats.joinToString(", ")

        println("- [${screeningMovie.movie.title.value}] $date $movieTime 좌석: $seatNumbers")
    }
}
