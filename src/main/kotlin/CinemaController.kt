import model.CinemaKiosk
import model.CinemaTime
import model.MovieReservationResult
import model.movie.Movie
import model.movie.MovieCatalog
import model.payment.MoviePayment
import model.payment.PayType
import model.schedule.MovieScreening
import model.schedule.onDate
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
    val movieCatalog: MovieCatalog,
) {
    fun run() {
        if (startReservation().not()) return
        do {
            // 영화 예매
            val selectedMovie = selectMovie(movieCatalog)
            val movieScreening = getMovieSchedule(selectedMovie)

            // 날짜 선택하고 해당 날짜의 상영 일정 중 선택
            val selectedDate = selectDate()
            val onDateMovieScreening = movieScreening.onDate(selectedDate)
            OutputView.showMovieScreenings(onDateMovieScreening)
            val selectMovieScreening = selectMovieScreening(onDateMovieScreening)
            OutputView.showMovieSeatGroup(selectMovieScreening)

            reserveSeats(selectMovieScreening)
        } while (InputView.askReserveMore())
        // 결제
        val totalPrice = processPayment(cinemaKiosk)
        OutputView.showTotalPrice(totalPrice)
    }

    private fun startReservation(): Boolean = InputView.askStartReservation()

    private fun selectMovie(movieCatalog: MovieCatalog): Movie {
        while (true) {
            val input = InputView.inputMovieName()
            val movie = movieCatalog.findByName(input)
            if (movie != null) return movie
            OutputView.showInvalidMovieName()
        }
    }

    private fun getMovieSchedule(movie: Movie): List<MovieScreening> = cinemaKiosk.cinemaSchedule.getMovieScreenings(movie.id)

    private fun selectDate(): CinemaTime {
        while (true) {
            try {
                val input = InputView.inputDate()
                return input
            } catch (e: Exception) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }

    private fun selectMovieScreening(onDateMovieScreening: List<MovieScreening>): MovieScreening {
        while (true) {
            try {
                val number = InputView.selectMovieScreening()
                val index = number - 1
                require(index in onDateMovieScreening.indices) { "올바르지 않은 번호입니다." }
                return onDateMovieScreening[index]
            } catch (e: Exception) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }

    private fun reserveSeats(selectMovieScreening: MovieScreening): List<MovieReservationResult.Success> {
        while (true) {
            try {
                val selectSeats = InputView.selectSeats()
                val reservations =
                    cinemaKiosk.reserveSeats(
                        movieScreening = selectMovieScreening,
                        selectedSeats = selectSeats,
                    )
                OutputView.showReservationInfo(reservations)
                return reservations
            } catch (e: IllegalArgumentException) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }

    private fun processPayment(cinemaKiosk: CinemaKiosk): Int {
        val moviePayment = MoviePayment(cinemaKiosk.reserveResults)
        while (true) {
            try {
                OutputView.showShoppingCart(successResults = cinemaKiosk.reserveResults)
                val point = InputView.inputPoint()
                val selectedPayType = InputView.inputPayType()
                val payType = PayType.fromId(selectedPayType)
                return moviePayment.getFinalPrice(
                    payType = payType,
                    point = point,
                )
            } catch (e: IllegalArgumentException) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }
}
