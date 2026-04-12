import model.movie.MovieName
import model.payment.DefaultMoviePayment
import model.payment.EarlyMorningDiscount
import model.payment.LateNightDiscount
import model.payment.MovieDayDiscount
import model.payment.PayType
import model.payment.PayTypeDiscount
import model.payment.Point
import model.payment.PointDiscount
import model.payment.SequentialMovieDiscount
import model.payment.SequentialPaymentDiscount
import model.reservation.MovieReservationGroup
import model.schedule.CinemaSchedule
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import model.seat.SeatPosition
import model.time.CinemaTime
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaSchedule: CinemaSchedule,
) {
    fun run(movieReservationGroup: MovieReservationGroup = MovieReservationGroup(emptySet())) {
        var movieReservationGroup = movieReservationGroup
        if (startMovieReservation().not()) return
        do {
            val movieScheduleOne = getMovieSchedule()
            val date = inputDate()
            val movieSchedule = movieScheduleOne.getMovieSchedule(date)
            OutputView.showMovieSchedule(movieSchedule)
            val movieScreening = selectMovieScreening(movieSchedule)
            OutputView.showSeatGroup(movieScreening.seatGroup)
            println("${movieScreening.screenTime.start}")
            movieReservationGroup =
                reserveSeats(
                    movieScreening.movie.name,
                    movieScreening.screenTime.start,
                    cinemaSchedule = cinemaSchedule,
                    movieReservationGroup,
                )
        } while (inputContinue())
        OutputView.showMovieReservationResult("장바구니", movieReservationGroup)
        val point = Point(inputPoint())
        val payType = inputPayType()
        val defaultMoviePayment =
            DefaultMoviePayment(
                reservations = movieReservationGroup,
                sequentialMovieDiscount =
                    SequentialMovieDiscount(
                        listOf(
                            MovieDayDiscount(),
                            EarlyMorningDiscount(),
                            LateNightDiscount(),
                        ),
                    ),
                sequentialPaymentDiscount =
                    SequentialPaymentDiscount(
                        listOf(
                            PointDiscount(point = point),
                            PayTypeDiscount(payType),
                        ),
                    ),
            )
        OutputView.printTotalPrice(defaultMoviePayment.calculate())
        val confirm = inputConfirmPayment()
        if (confirm) {
            OutputView.showMovieReservationResult("예매 완료\n내역:", movieReservationGroup)
        }
        OutputView.end()
    }

    private fun startMovieReservation(): Boolean {
        while (true) {
            try {
                val start = InputView.startMovieReservation("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
                return start
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun getMovieSchedule(): MovieSchedule {
        while (true) {
            val movieName = InputView.inputMovieName()
            if (movieName != null) {
                val movieSchedule = cinemaSchedule[movieName]
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
        cinemaSchedule: CinemaSchedule,
        initialMovieReservationGroup: MovieReservationGroup,
    ): MovieReservationGroup {
        while (true) {
            try {
                val positions = InputView.selectSeats()
                val finalMovieReservationGroup =
                    positions.fold(initialMovieReservationGroup) { group, (row, column) ->
                        group.reserve(
                            cinemaSchedule = cinemaSchedule,
                            movieName = movieName,
                            startTime = startTime,
                            seatPosition =
                                SeatPosition(
                                    row = row,
                                    column = column,
                                ),
                        )
                    }
                OutputView.showMovieReservationResult(
                    initialMessage = "장바구니에 추가됨",
                    reservationGroup = finalMovieReservationGroup - initialMovieReservationGroup,
                )
                return finalMovieReservationGroup
            } catch (e: Exception) {
                println("예약 중 문제가 발생했습니다 ${e.message}")
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
