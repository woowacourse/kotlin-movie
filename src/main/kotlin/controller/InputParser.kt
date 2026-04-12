package controller

import domain.movie.Title
import domain.payment.PaymentType
import domain.payment.Point
import domain.seat.Column
import domain.seat.Row
import domain.seat.SeatPosition
import domain.seat.SeatPositions
import java.time.LocalDate
import java.time.format.DateTimeParseException

object InputParser {
    private val SEAT_FORMAT_REGEX = "([a-zA-Z]+)(\\d+)".toRegex()

    fun parseYN(input: String): Boolean =
        when (input.lowercase()) {
            "y" -> true
            "n" -> false
            else -> throw IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세용가리치킨")
        }

    fun parseMovieTitle(input: String): Title = Title(input)

    fun parseDate(input: String): LocalDate =
        try {
            LocalDate.parse(input)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("잘못된 형식입니다. 다시 입력해주세용가리치킨")
        }

    fun parseNum(input: String): Int = input.toInt()

    fun parseSeats(input: String): SeatPositions {
        val formatted =
            input.split(",").map { it ->
                val matchResult = SEAT_FORMAT_REGEX.find(it.trim())
                val (full, rawR, rawC) =
                    matchResult?.groupValues
                        ?: throw IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세용가리치킨")
                SeatPosition(Row(rawR), Column(rawC.toInt()))
            }

        return SeatPositions(formatted)
    }

    fun parsePoint(input: String): Point = Point(input.toInt())

    fun parsePaymentType(input: String): PaymentType =
        when (input.toInt()) {
            1 -> PaymentType.CREDIT_CARD
            2 -> PaymentType.CASH
            else -> throw IllegalArgumentException("잘못된 입력입니다. 다시 입력해주세용가리치킨")
        }
}
