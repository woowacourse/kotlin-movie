package movie.view

import movie.domain.Point
import movie.domain.Price
import movie.domain.movie.Movie
import movie.domain.movie.Reservation
import movie.domain.movie.ScreeningMovie
import movie.domain.movie.ScreeningMovies
import movie.domain.movie.Ticket
import movie.domain.payment.PaymentMethod

object OutputView {
    fun printMovieStartTimes(movie: Movie, screeningMovies: ScreeningMovies) {
        println("해당 날짜의 상영 목록")
        screeningMovies.getMovieTimes(movie).forEachIndexed { index, time ->
            println("[${index + 1}] ${time.startTime.hour}:${time.startTime.minute}")
        }
    }

    fun printSeats(screeningMovie: ScreeningMovie) {
        val seats = screeningMovie.theater.seats.getSeats()

        println("좌석 배치도")

        val groupedByRow = seats
            .groupBy { it.seatNumber.row.toString() }
            .toSortedMap()

        val firstRowSeats = groupedByRow.values.firstOrNull().orEmpty()
            .sortedBy { it.seatNumber.col.toString() }

        val header = buildString {
            append("    ")
            firstRowSeats.forEach { seat ->
                append(seat.seatNumber.col.toString().padStart(4))
            }
        }
        println(header)

        groupedByRow.forEach { (row, rowSeats) ->
            val rowText = buildString {
                append(row)
                append(" ")
                rowSeats
                    .sortedBy { it.seatNumber.col.toString() }
                    .forEach { seat ->
                        when (screeningMovie.isReserved(seat.seatNumber)) {
                            true -> append("[ X] ")
                            false -> append("[ ${seat.rank.name}] ")
                        }
                    }
            }
            println(rowText)
        }
    }

    fun printReservationAddMessage(reservation: Reservation) {
        println("장바구니에 추가됨")
        printReservations(reservation)
    }

    fun printCart(ticket: Ticket) {
        println("장바구니")

        val reservations = ticket.getReservations()
        reservations.forEach {
            printReservations(it)
        }
    }


    fun printTotalPrice(totalPrice: Price) {
        println("가격 계산")
        println("최종 결제 금액: ${formatWithComma(totalPrice.value)}원")
    }


    fun printReceipt(ticket: Ticket, paymentPrice: Price, usePoint: Point) {
        println("예매 완료")
        println("내역:")

        val reservations = ticket.getReservations()
        reservations.forEach {
            printReservations(it)
        }

        val usePointString = formatWithComma(usePoint.value)
        val paymentPriceString = formatWithComma(paymentPrice.value)

        println("결제 금액: ${paymentPriceString}원  (포인트 ${usePointString}원 사용)")

    }

    fun printThankYou() {
        println("감사합니다.")
    }

    fun formatWithComma(value: Int): String {
        return "%,d".format(value)
    }

    private fun printReservations(reservation: Reservation) {
        val screeningMovie = reservation.screeningMovie
        val movieTime =
            "${screeningMovie.movieTime.startTime.hour}:${screeningMovie.movieTime.startTime.minute}"
        val seatNumbers = reservation.seatNumbers.joinToString(", ")
        println("- [${screeningMovie.movie.title.value}] ${screeningMovie.movieTime.date} $movieTime 좌석: $seatNumbers")
    }
}
