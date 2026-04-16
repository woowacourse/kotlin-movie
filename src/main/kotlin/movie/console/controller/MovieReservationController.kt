package movie.console.controller

import movie.console.view.InputView
import movie.console.view.OutputView
import movie.model.movie.Movie
import movie.model.payment.PayResult
import movie.model.payment.PaymentSystem
import movie.model.policy.DiscountSystem
import movie.model.policy.MovieDayDiscountPolicy
import movie.model.policy.TimeDiscountPolicy
import movie.model.reservation.Reservation
import movie.model.reservation.Reservations
import movie.model.screening.Screening
import movie.model.screening.ScreeningSeatMap
import movie.model.screening.Screenings
import movie.repository.MovieRepository
import movie.repository.ReservationRepository
import movie.repository.ReservedSeatRepository
import movie.repository.ScreeningRepository

class MovieReservationController(
    private val screeningRepository: ScreeningRepository,
    private val movieRepository: MovieRepository,
    private val reservationRepository: ReservationRepository,
    private val reservedSeatRepository: ReservedSeatRepository,
    private val inputView: InputView,
    private val outputView: OutputView,
) {
    fun run() {
        // 예매 시작 후 볼 영화 모두 고르기
        val isStartReservation = retryUntilValid { inputView.readStartReservation() }
        if (!isStartReservation) return
        val reservations = makeCart()

        // 장바구니 전체 출력
        outputView.printCart(reservations)

        // 구매 단계
        val payResult = purchase(reservations)

        // 최종 결제 금액 출력
        outputView.printFinalPrice(payResult.finalPrice)

        // 결제 여부 입력
        val isPaymentConfirmation = retryUntilValid { inputView.readPaymentConfirmation() }
        if (!isPaymentConfirmation) return

        // 예매 완료 전체 출력
        outputView.printReceipt(reservations, payResult)

        // 레포지터리에 저장
        save(reservations)
    }

    private fun makeCart(): Reservations {
        var reservations = Reservations()

        do {
            val reservation = makeReservation(reservations)
            reservations = reservations.add(reservation)
            outputView.printCartItem(reservation)
        } while (inputView.readAddMoreMovie())

        return reservations
    }

    private fun purchase(reservations: Reservations): PayResult {
        val point = retryUntilValid { inputView.readPoint() }
        val paymentMethod = retryUntilValid { inputView.readPaymentMethod() }

        val discountPolicies = listOf(MovieDayDiscountPolicy, TimeDiscountPolicy)
        val discountSystem = DiscountSystem(discountPolicies)
        val paymentSystem = PaymentSystem()

        val discountedPrice = discountSystem.discountPrice(reservations)
        return paymentSystem.pay(paymentMethod, discountedPrice, point)
    }

    private fun makeReservation(currentReservations: Reservations): Reservation {
        val movie = searchMovie()
        val screenings = searchScreenings(movie)
        outputView.printScreenings(screenings)
        val selectedScreening = selectScreening(screenings, currentReservations)
        outputView.printSeatMap(
            selectedScreening.seatMap.screen.seats,
            selectedScreening.seatMap.getAvailableSeats(),
        )
        return retryUntilValid {
            val seatNumbers = inputView.readSeatsNumber()
            selectedScreening.reserve(seatNumbers)
        }
    }

    private fun searchMovie(): Movie {
        while (true) {
            val title = inputView.readMovieTitle()
            val movie = movieRepository.findByTitle(title)
            if (movie != null) return movie
            outputView.printMovieNotSearch()
        }
    }

    private fun searchScreenings(movie: Movie): Screenings =
        retryUntilValid {
            val date = inputView.readDate()
            val initialScreenings = screeningRepository.findBy(movie, date)
            if (initialScreenings.isEmpty()) throw IllegalArgumentException("해당 날짜의 상영 목록이 없습니다.")

            val updatedScreeningList =
                initialScreenings.map { screening ->
                    val reservedSeats = reservedSeatRepository.findSeatsByScreening(screening)
                    val updateSeatMap = ScreeningSeatMap(screening.seatMap.screen, reservedSeats)
                    screening.copy(seatMap = updateSeatMap)
                }
            Screenings(updatedScreeningList)
        }

    private fun selectScreening(
        screenings: Screenings,
        currentReservations: Reservations,
    ): Screening =
        retryUntilValid {
            val index = inputView.readScreeningNumber() - 1
            val screening =
                screenings.elementAtOrNull(index) ?: throw IllegalArgumentException("없는 상영 번호입니다.")

            val reservationsInRepo = reservationRepository.findByDate(screening.showDate)

            if (currentReservations.hasOverlappingWith(screening) or
                reservationsInRepo.hasOverlappingWith(screening)
            ) {
                throw IllegalArgumentException("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
            }
            screening
        }

    private fun save(reservations: Reservations) {
        reservations.forEach { reservation ->
            reservationRepository.save(reservation)
            reservedSeatRepository.save(reservation.screening, reservation.seats)
        }
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
