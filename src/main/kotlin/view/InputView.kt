package view

import model.CinemaTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object InputView {
    fun startMovieReservation(text: String): Boolean {
        println(text)
        val input = readln()
        InputValidator.validateYesOrNo(input)
        return input == "Y"
    }

    fun inputMovieName(): String {
        println(Message.INPUT_MOVIE_NAME)
        return readln()
    }

    fun inputDate(): CinemaTime {
        println(Message.INPUT_DATE)
        val input = readln()
        val date =
            runCatching {
                LocalDate.parse(
                    input,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                )
            }.getOrElse {
                throw IllegalArgumentException("날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)")
            }
        return CinemaTime(date.atStartOfDay())
    }
}
