package console.view

import domain.movie.Title
import domain.payment.PaymentType
import domain.common.Point
import domain.seat.Column
import domain.seat.Row
import domain.seat.SeatPosition
import domain.seat.SeatPositions
import java.time.LocalDate
import java.time.format.DateTimeParseException

object InputView {
    private val SEAT_FORMAT_REGEX = "([a-zA-Z]+)(\\d+)".toRegex()

    private fun read(): String = readln().trim()

    fun readYN(): Boolean =
        when (read().lowercase()) {
            "y" -> true
            "n" -> false
            else -> throw IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세용가리치킨")
        }

    fun readMovieTitle(): Title = Title(read())

    fun readDate(): LocalDate =
        try {
            LocalDate.parse(read())
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("잘못된 형식입니다. 다시 입력해주세용가리치킨")
        }

    fun readNum(): Int =
        read().toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요.")

    fun readSeats(): SeatPositions {
        val input = read()
        val formatted =
            input.split(",").map {
                val matchResult = SEAT_FORMAT_REGEX.find(it.trim())
                val (_, rawR, rawC) =
                    matchResult?.groupValues
                        ?: throw IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세용가리치킨")
                SeatPosition(Row(rawR), Column(rawC.toInt()))
            }

        return SeatPositions(formatted)
    }

    fun readPoint(): Point =
        Point(read().toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요."))

    fun readPaymentType(): PaymentType =
        when (read().toIntOrNull()) {
            1 -> PaymentType.CREDIT_CARD
            2 -> PaymentType.CASH
            else -> throw IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세용가리치킨")
        }
}
