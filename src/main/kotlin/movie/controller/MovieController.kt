package movie.controller

import movie.data.MovieData
import movie.domain.movie.Movie
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.seat.Seat
import movie.domain.seat.SelectedSeats
import movie.domain.user.User
import movie.view.InputView
import movie.view.OutputView
import java.time.LocalDate

class MovieController(
    private val inputView: InputView = InputView(),
    private val outputView: OutputView = OutputView(),
    private val movies: List<Movie> = MovieData.createMovies(),
    private val user: User = MovieData.createUser(),
) {
    fun run(){
        //예매 여부 확인
        if (!askStartReservation()) return

        //예매하기
        val reservations = collectReservations()
        //장바구니 담기
        outputView.printCart(reservations)
    }

    private fun askStartReservation(): Boolean =
        executeWithRetry { inputView.startMessage() }

    private fun collectReservations(): Reservations {
        val reservationList = mutableListOf<Reservation>()
        do {
            val reservation = selectMovieAndSeats(reservationList)
            reservationList.add(reservation)
        } while (askAddMore())

        return Reservations(reservationList)
    }
    private fun askAddMore(): Boolean =
        executeWithRetry { inputView.addMoreMovie() }

    private fun selectMovieAndSeats(existingReservations: List<Reservation>): Reservation {
        val movie = selectMovie()
        val date = selectDate(movie)
        val screening = selectScreening(movie, date, existingReservations)
        val seats = selectSeats(screening)
        screening.reserve(seats)
        val reservation = Reservation(screening, SelectedSeats(seats))
        outputView.printAddedToCart(reservation)
        return reservation
    }

    private fun selectMovie(): Movie =
        executeWithRetry {
            val title = inputView.inputMovieTitle()
            movies.find { it.title == title }
                ?: throw IllegalArgumentException("존재하지 않는 영화입니다.")
        }

    private fun selectDate(movie: Movie): LocalDate =
        executeWithRetry {
            val input = inputView.inputDate()
            val date = LocalDate.parse(input.toString())
            require(movie.hasScreeningOnDate(date)) { "해당 날짜에 상영이 없습니다." }
            date
        }

    private fun selectScreening(
        movie: Movie,
        date: LocalDate,
        existingReservations: List<Reservation>,
    ): Screening =
        executeWithRetry {
            val screenings = movie.getScreeningsByDate(date)
            outputView.printScreeningList(screenings)

            val number = inputView.inputScreeningNumber()
            require(number in 1..screenings.size) { "유효하지 않은 상영 번호입니다." }

            val selected = screenings[number - 1]

            val hasOverlap = existingReservations.any {
                it.isTimeOverlapping(selected)
            }

            if (hasOverlap) {
                outputView.printTimeOverlapMessage()
                throw IllegalArgumentException()
            }
            selected
        }

    private fun selectSeats(screening: Screening): List<Seat> =
        executeWithRetry {
            outputView.printSeatLayout(screening.screen, screening.reservatedSeats)
            val input = inputView.inputSeat()
            val seats = parseSeatInput(input, screening.screen)
            val reserveAvailableSeats = screening.isReserveAvailable(seats)
            reserveAvailableSeats
        }

    private fun parseSeatInput(input: String, screen: Screen): List<Seat> {
        return input.split(",")
            .map { it.trim() }
            .map { seatInput ->
                val row = seatInput.substring(0, 1).uppercase()
                val column = seatInput.substring(1).toIntOrNull()
                    ?: throw IllegalArgumentException("유효하지 않은 좌석 형식입니다: $seatInput")
                screen.seats.findSeat(row, column)
            }
    }

    private fun <T> executeWithRetry(block: () -> T): T {
        while (true) {
            try {
                return block()
            } catch (e: IllegalArgumentException) {
                if (!e.message.isNullOrBlank()) {
                    outputView.printErrorMessage(e.message!!)
                }
            }
        }
    }
}
