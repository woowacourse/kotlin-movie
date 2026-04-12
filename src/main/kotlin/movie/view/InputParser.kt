package movie.view

import movie.domain.MovieTitle
import movie.domain.point.Point
import movie.domain.seat.SeatNumber
import java.time.LocalDate

object InputParser {
    fun parseYesNo(input: String): Boolean = input.uppercase() == "Y"

    fun parseDate(input: String): LocalDate = LocalDate.parse(input)

    fun parseNumber(input: String): Int = input.toInt()

    fun parsePoint(input: String): Point = Point(input.toInt())

    fun parseSeatNumbers(input: String): List<SeatNumber> =
        input
            .split(",")
            .map { it.trim() }
            .map { seat ->
                val row = seat.first()
                val col = seat.substring(1).toInt()

                SeatNumber(
                    row = row,
                    col = col,
                )
            }


    fun parseIndex(
        input: String,
        size: Int,
    ): Int {
        val index = input.toInt()
        require(index in 1..size) { "유효한 번호를 입력해주세요" }

        return index - 1
    }

    fun parseMovieTitle(input: String): MovieTitle {
        return MovieTitle(input)
    }
}
