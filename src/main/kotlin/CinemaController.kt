import database.repository.MovieScreeningRepository
import database.repository.ReservationRepository
import model.CinemaKiosk
import model.CinemaTime
import model.payment.MoviePayment
import model.payment.PayType
import model.payment.policy.discount.EarlyLateDiscount
import model.payment.policy.discount.MovieDayDiscount
import model.payment.policy.discount.PayTypeDiscount
import model.payment.policy.discount.PointDiscount
import model.reservation.MovieReservationResult
import model.schedule.MovieScreening
import model.schedule.onDate
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
    val screeningRepository: MovieScreeningRepository,
    val reservationRepository: ReservationRepository,
) {
    fun run() {
        if (startReservation().not()) return
        val reservationId = reservationRepository.createReservation()
        do {
            // 영화 예매
            val movieScreening = selectMovieScreenings()
            // 날짜 선택하고 해당 날짜의 상영 일정 중 선택
            val selectedDate = selectDate()
            val onDateMovieScreening = movieScreening.onDate(selectedDate)
            OutputView.showMovieScreenings(onDateMovieScreening)
            val selectMovieScreening = selectMovieScreening(onDateMovieScreening)
            OutputView.showMovieSeatGroup(selectMovieScreening)
            reserveSeats(selectMovieScreening, reservationId)
        } while (InputView.askReserveMore())
        // 결제
        processPayment(reservationId)
        OutputView.end()
    }

    private fun startReservation(): Boolean {
        while (true) {
            try {
                return InputView.askStartReservation()
            } catch (e: IllegalArgumentException) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }

    private fun selectMovieScreenings(): List<MovieScreening> {
        while (true) {
            val name = InputView.inputMovieName()
            val screenings = screeningRepository.findScreeningsByMovieName(name)
            if (screenings != null) return screenings
            OutputView.showInvalidMovieName()
        }
    }

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

    private fun reserveSeats(
        selectMovieScreening: MovieScreening,
        reservationId: Int,
    ): List<MovieReservationResult.Success> {
        while (true) {
            try {
                val selectSeats = InputView.selectSeats()
                val reservations =
                    cinemaKiosk.reserveSeats(
                        movieScreening = selectMovieScreening,
                        selectedSeats = selectSeats,
                    )
                reservations.forEach { reservation ->
                    val screeningId =
                        screeningRepository.findScreeningId(
                            name = reservation.movie.toString(),
                            screenStart = reservation.screenTime.start.format("yyyy-MM-dd'T'HH:mm"),
                        )
                    reservationRepository.saveSeat(reservationId, screeningId!!, reservation.seat)
                }
                OutputView.showReservationInfo(reservations)
                return reservations
            } catch (e: IllegalArgumentException) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }

    private fun processPayment(reservationId: Int) {
        while (true) {
            try {
                OutputView.showShoppingCart(successResults = cinemaKiosk.reserveResults)
                val point = InputView.inputPoint()
                val selectedPayType = InputView.inputPayType()
                val payType = PayType.fromId(selectedPayType)
                val moviePayment =
                    MoviePayment(
                        reservations = cinemaKiosk.reserveResults,
                        policies =
                            listOf(
                                MovieDayDiscount(),
                                EarlyLateDiscount(),
                                PointDiscount(point),
                                PayTypeDiscount(),
                            ),
                    )
                val finalPrice = moviePayment.getFinalPrice(payType)
                OutputView.showTotalPrice(finalPrice)
                val isConfirm = askPaymentConfirm(finalPrice, point, reservationId)

                if (isConfirm) {
                    reservationRepository.updatePayment(
                        reservationId = reservationId,
                        totalPrice = finalPrice,
                        usedPoint = point,
                        payType = payType,
                    )
                }
                return
            } catch (e: IllegalArgumentException) {
                OutputView.showErrorMessage(e.message)
            }
        }
    }

    private fun askPaymentConfirm(
        finalPrice: Int,
        point: Int,
        reservationId: Int,
    ): Boolean {
        if (InputView.askPaymentConfirm()) {
            OutputView.totalReservation(
                successResults = cinemaKiosk.reserveResults,
                price = finalPrice,
                point = point,
            )
            return true
        }

        reservationRepository.delete(reservationId)
        return false
    }
}
