import model.CinemaKiosk
import model.CinemaTime
import model.MovieReservationResult
import model.movie.MovieCatalog
import model.movie.MovieName
import model.payment.MoviePayment
import model.payment.PayType
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
    val movieCatalog: MovieCatalog,
) {
    fun run() {
        if (startMovieReservation().not()) return
        do {
            val movieScheduleOne = getMovieSchedule()
            val date = inputDate()
            val movieSchedule = movieScheduleOne.getMovieSchedule(date)
            OutputView.showMovieSchedule(movieSchedule)
            val movieScreening = selectMovieScreening(movieSchedule)
            OutputView.showSeatGroup(movieScreening.seatGroup)
            reserveSeats(movieScreening.movie.name, movieScreening.screenTime.start)
        } while (inputContinue())
        OutputView.showShoppingCart(cinemaKiosk.reserveResults)
        val point = inputPoint()
        val payType = inputPayType()
        val payment =
            MoviePayment(
                reservations = cinemaKiosk.reserveResults,
            )
        payment.discount()
        payment.applyPoint(point)
        OutputView.printTotalPrice(payment.pay(payType))
        val confirm = inputConfirmPayment()
        if (confirm) {
            OutputView.totalReservation(
                successResults = cinemaKiosk.reserveResults,
                price = payment.currentPrice,
                point = point,
            )
        }
        OutputView.end()
    }

    private fun startMovieReservation(): Boolean {
        while (true) {
            try {
                val start = InputView.startMovieReservation(Message.START_RESERVATION)
                return start
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun getMovieSchedule(): MovieSchedule {
        while (true) {
            val movie = movieCatalog.findByName(InputView.inputMovieName())
            if (movie != null) {
                val movieSchedule = cinemaKiosk.cinemaSchedule.getMovieSchedule(movie.id)
                if (movieSchedule.isEmpty().not()) return movieSchedule
            }
        }
    }

    private fun inputDate(): CinemaTime {
        while (true) {
            try {
                return InputView.inputDate()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun selectMovieScreening(movieSchedule: MovieSchedule): MovieScreening {
        while (true) {
            try {
                return InputView.selectMovieScreening(movieSchedule)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun reserveSeats(
        movieName: MovieName,
        startTime: CinemaTime,
    ) {
        while (true) {
            try {
                val positions = InputView.selectSeats()
                val successPositions = mutableListOf<MovieReservationResult.Success>()

                for (position in positions) {
//                    val result =
//                        cinemaKiosk.reserve(
//                            movieName = movieName,
//                            startTime = startTime,
//                            seatRow = position.first,
//                            seatColumn = position.second,
//                        )
//                    when (result) {
//                        is MovieReservationResult.Success -> successPositions.add(result)
//                        is MovieReservationResult.Failed -> {
//                            val successPositions = successPositions.map { it.seat.row to it.seat.column }
//                            cinemaKiosk.cancelReservations(movieName, startTime, successPositions)
//                            throw IllegalArgumentException("예약에 실패했습니다.")
//                        }
//                    }
                }
                OutputView.showReservationInfo(successPositions)
                return
            } catch (_: Exception) {
                println("문제가 발생했습니다.")
            }
        }
    }

    private fun inputContinue(): Boolean {
        while (true) {
            try {
                return InputView.inputContinue()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun inputPoint(): Int {
        while (true) {
            try {
                return InputView.inputPoint()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun inputPayType(): PayType {
        while (true) {
            try {
                val input = InputView.inputPaymentMethod()
                return selectTypeNumber(input)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun inputConfirmPayment(): Boolean {
        while (true) {
            try {
                return InputView.inputConfirmPayment()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun selectTypeNumber(number: Int): PayType {
        if (number == 1) return PayType.CREDIT_CARD
        if (number == 2) return PayType.CASH
        throw IllegalArgumentException("존재하지 않는 선택번호입니다")
    }
}
