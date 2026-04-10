package controller

import model.Scheduler
import model.movie.Movie
import model.payment.PaymentSystem
import model.policy.DiscountSystem
import model.reservation.Reservation
import model.reservation.Reservations
import model.screening.Screening
import model.screening.Screenings
import view.InputView
import view.OutputView

class MovieReservationController(
    private val scheduler: Scheduler,
    private val inputView: InputView,
    private val outputView: OutputView,
) {
    fun run() {
        // 예매 시작
        val isStartReservation = retryUntilValid { inputView.readStartReservation() }
        if (!isStartReservation) return
        val reservations = makeCart()

        // 장바구니 전체 출력
        outputView.printCart(reservations)

        // 포인트 입력
        val point = retryUntilValid { inputView.readPoint() }

        // 결제 수단 선택
        val paymentMethod = retryUntilValid { inputView.readPaymentMethod() }

        // 할인 시스템 가동
        val discountSystem = DiscountSystem()
        val discountedPrice = discountSystem.discountPrice(reservations)

        // 결제 시스템 가동
        val paymentSystem = PaymentSystem(paymentMethod)
        val payResult = paymentSystem.pay(discountedPrice, point)

        // 최종 결제 금액 출력
        outputView.printFinalPrice(payResult.finalPrice)

        // 결제 여부 입력
        val isPaymentConfirmation = retryUntilValid { inputView.readPaymentConfirmation() }
        if (!isPaymentConfirmation) return

        // 예매 완료 전체 출력
        outputView.printReceipt(reservations, payResult)
    }

    private fun makeCart(): Reservations {
        var reservations = Reservations()

        do {
            val reservation = makeReservation(reservations)
            reservations = reservations.addReservation(reservation)
            outputView.printCartItem(reservation)
        } while (inputView.readAddMoreMovie())

        return reservations
    }

    private fun makeReservation(currentReservations: Reservations): Reservation {
        val movie = searchMovie()
        val screenings = searchScreenings(movie)
        outputView.printScreenings(screenings)
        val selectedScreening = selectScreening(screenings, currentReservations)
        outputView.printSeatMap(
            selectedScreening.screen.seats,
            selectedScreening.availableSeats(),
        )
        return retryUntilValid {
            val seatsNumber = inputView.readSeatsNumber()
            selectedScreening.reserve(seatsNumber)
        }
    }

    private fun searchMovie(): Movie {
        while (true) {
            val title = inputView.readMovieTitle()
            val movie = scheduler.getMovies().findByTitle(title)
            if (movie != null) return movie
            outputView.printMovieNotSearch()
        }
    }

    private fun searchScreenings(movie: Movie): Screenings =
        retryUntilValid {
            val date = inputView.readDate()
            val screenings = scheduler.getScreenings(movie, date)
            if (screenings.isEmpty()) throw IllegalArgumentException("해당 날짜의 상영 목록이 없습니다.")
            screenings
        }

    fun selectScreening(
        screenings: Screenings,
        currentReservations: Reservations,
    ): Screening =
        retryUntilValid {
            val index = inputView.readScreeningNumber() - 1
            val screening =
                screenings.elementAtOrNull(index)
                    ?: throw IllegalArgumentException("없는 상영 번호입니다.")
            if (currentReservations.isOverlapping(screening)) throw IllegalArgumentException("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
            screening
        }

    private fun <T> retryUntilValid(action: () -> T): T {
        while (true) {
            try {
                return action()
            } catch (e: IllegalArgumentException) {
                outputView.printError(e.message ?: "알수없는 예외가 발생했습니다.")
            }
        }
    }
}
