package view

import model.CinemaTime
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
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

    fun selectMovieScreening(movieSchedule: MovieSchedule): MovieScreening {
        val input = readln()
        require(input.all { it.isDigit() }) { "숫자만 가능합니다" }
        val index = input.toInt() - 1
        require(index in (0..<movieSchedule.size)) { "올바르지 않은 번호입니다." }
        return movieSchedule[index]
    }
}
