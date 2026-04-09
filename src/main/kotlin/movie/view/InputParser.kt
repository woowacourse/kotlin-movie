package movie.view

import movie.domain.Point
import movie.domain.seat.number.Column
import movie.domain.seat.number.Row
import movie.domain.seat.number.SeatNumber
import java.time.LocalDate

object InputParser {
    fun parseYesNo(input: String): Boolean {
        return input.uppercase() == "Y"
    }

    fun parseDate(input: String): LocalDate {
        return LocalDate.parse(input)
    }

    fun parseNumber(input: String): Int {
        return input.toInt()
    }

    fun parsePoint(input: String): Point {
        return Point(input.toInt())
    }

    fun parseSeatNumbers(input: String): List<SeatNumber> {
        return input.split(",")
            .map { it.trim() }
            .map { seat ->
                val row = seat.first()
                val col = seat.substring(1).toInt()

                SeatNumber(
                    row = Row(row),
                    col = Column(col),
                )
            }
    }
}
