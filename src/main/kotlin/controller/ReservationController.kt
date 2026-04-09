package controller

import domain.Movie
import domain.MovieTheater
import domain.Reservation
import domain.Screen
import domain.Seat
import domain.Showing
import kotlinx.datetime.LocalDate

class ReservationController(val movieTheater: MovieTheater) {
    fun run(): Pair<Showing, List<Seat>> {
        create()
        val movie = chooseMovie()
        val date = chooseDate(movie)
        val showing = chooseShowingTime(movie, date)
        val seats = chooseSeat(showing)

        return showing to seats
    }

    fun create(): Boolean {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        val input = readln()

        require(input == "Y" || input == "N") { "입력값은 Y 혹은 N이어야 합니다." }

        when (input) {
            "Y" -> return true
        }
        return false
    }

    fun chooseMovie(): Movie {
        println("예매할 영화 제목을 입력하세요:")
        val input = readln()

        val movieIndex = movieTheater.movies.indexOfFirst { it.title == input }

        require(movieIndex != -1) { "존재하지 않는 영화입니다." }

        return movieTheater.movies[movieIndex]
    }

    fun chooseDate(movie: Movie): LocalDate {
        println("날짜를 입력하세요 (YYYY-MM-DD):")
        val input = readln()

        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)" }

        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        require(showings.isNotEmpty()) { "해당 날짜에 선택한 영화의 상영이 없습니다." }

        return date
    }

    fun chooseShowingTime(
        movie: Movie,
        date: LocalDate,
    ): Showing {
        val showings = movieTheater.showings.filter { it.movie == movie && it.startTime.date == date }
        println("해당 날짜의 상영 목록")

        showings.forEachIndexed { index, showing ->
            println("[${index + 1}] ${showing.startTime.time}")
        }
        println("상영 번호를 선택하세요:")
        val input = readln()

        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { "선택하신 상영 번호는 없는 상영 번호입니다." }

        Reservation.checkReservationHistory(movieTheater.reservationInfos, showings[input.toInt() - 1])

        return showings[input.toInt() - 1]
    }

    fun chooseSeat(showing: Showing): List<Seat> {
        println("좌석 배치도")
        val screen = showing.screen
        val maxRow = Screen.MAX_ROW
        val maxColumn = Screen.MAX_COLUMN

        val header = " ".repeat(3) + (1..maxColumn).joinToString("    ")
        println(header)

        ('A' until 'A' + maxRow).forEach { row ->
            val line = "$row" + (1..maxColumn).joinToString("") { col ->
                val seat = screen.seats.find { it.row == row && it.column == col }
                " [${seat?.grade?.name ?: " "}]"
            }
            println(line)
        }
        println("예약할 좌석을 입력하세요 (A1, B2):")
        val input = readln()

        val seatInputs = input.split(',').map { it.trim() }

        val seats = seatInputs.map { seat ->
            Reservation.checkSeat(screen.seats, seat)
        }
        return seats
    }
}
