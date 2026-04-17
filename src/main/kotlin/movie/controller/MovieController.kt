package movie.controller

import movie.domain.amount.PaymentResult
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicy
import movie.domain.discount.DiscountPolicyAdapter
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.PaymentDiscountPolicy
import movie.domain.discount.PaymentMethodDiscountPolicy
import movie.domain.discount.PaymentMethodInputParser
import movie.domain.discount.TimeDiscount
import movie.domain.movie.Movie
import movie.domain.movie.Movies
import movie.domain.payment.PaymentMethod
import movie.domain.payment.PriceCalculator
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.seat.Seat
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import movie.domain.user.User
import movie.view.InputView
import movie.view.OutputView
import java.time.LocalDate

class MovieController(
    private val inputView: InputView,
    private val outputView: OutputView,
    private val movies: Movies,
    private val user: User,
    private val priceCalculator: PriceCalculator,
    private val onReservationComplete: (Reservations, PaymentResult, PaymentMethod) -> Unit = { _, _, _ -> },
) {
    fun run() {
        if (!askStartReservation()) return

        val reservations = collectReservations()
        val discountPolicy =
            DiscountPolicyAdapter(
                percentagePolicies = listOf(MovieDayDiscount()),
                fixedPolicies = listOf(TimeDiscount()),
            )
        val paymentDiscountPolicy = PaymentMethodDiscountPolicy()

        showCart(reservations)

        val (paymentResult, paymentMethod) = processPayment(discountPolicy, paymentDiscountPolicy, reservations)

        confirmAndComplete(reservations, paymentResult, paymentMethod)
    }

    private fun processPayment(
        discountPolicy: DiscountPolicy,
        paymentDiscountPolicy: PaymentDiscountPolicy,
        reservations: Reservations,
    ): Pair<PaymentResult, PaymentMethod> {
        val point = inputPoint()
        val paymentMethod = selectPaymentMethod()

        val paymentResult = priceCalculator.calculate(reservations, discountPolicy, paymentDiscountPolicy, point, paymentMethod)

        outputView.printFinalPrice(paymentResult.totalPrice)
        return Pair(paymentResult, paymentMethod)
    }

    private fun confirmAndComplete(
        reservations: Reservations,
        paymentResult: PaymentResult,
        paymentMethod: PaymentMethod,
    ) {
        val confirm = executeWithRetry { inputView.confirmPayment() }

        if (confirm) {
            onReservationComplete(reservations, paymentResult, paymentMethod)
            outputView.printComplete(
                reservations,
                paymentResult.totalPrice,
                paymentResult.usedPoint,
            )
        }
    }

    private fun showCart(reservations: Reservations) {
        outputView.printCart(reservations)
    }

    // 결제 상세 로직
    private fun inputPoint(): Point =
        executeWithRetry {
            val input = inputView.inputPoint()
            user.point.use(input)
        }

    private fun selectPaymentMethod(): PaymentMethod {
        val input = executeWithRetry { inputView.inputPayment() }
        return PaymentMethodInputParser.parse(input)
    }

    // 예매 로직
    private fun collectReservations(): Reservations {
        val reservationList = mutableListOf<Reservation>()
        do {
            val reservation = selectMovieAndSeats(reservationList)
            reservationList.add(reservation)
        } while (askAddMore())

        return Reservations(reservationList)
    }

    private fun selectMovieAndSeats(existingReservations: List<Reservation>): Reservation {
        val movie = selectMovie()
        val date = selectDate(movie)
        val screening = selectScreening(movie, date, existingReservations)
        val seats = selectSeats(screening)
        val updatedScreening = screening.reserve(seats)
        val reservation = Reservation(movie, updatedScreening, SelectedSeats(seats))
        outputView.printAddedToCart(reservation)
        return reservation
    }

    // 예매 상세 로직
    private fun selectMovie(): Movie =
        executeWithRetry {
            val title = inputView.inputMovieTitle()
            movies.findMovie(title)
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

            val hasOverlap =
                existingReservations.any {
                    it.isTimeOverlapping(selected)
                }

            if (hasOverlap) {
                outputView.printTimeOverlapMessage()
                throw IllegalArgumentException()
            }
            selected
        }

    private fun selectSeats(screening: Screening): Seats =
        executeWithRetry {
            outputView.printSeatLayout(screening.screen, screening.reservedSeats)
            val input = inputView.inputSeat()
            val seats = parseSeatInput(input, screening.screen)
            Seats(seats)
        }

    private fun parseSeatInput(
        input: String,
        screen: Screen,
    ): Set<Seat> =
        input
            .split(",")
            .map { it.trim() }
            .map { seatInput ->
                val row = seatInput.substring(0, 1).uppercase()
                val column =
                    seatInput.substring(1).toIntOrNull()
                        ?: throw IllegalArgumentException("유효하지 않은 좌석 형식입니다: $seatInput")
                screen.seats.findSeat(row, column)
            }.toSet()

    // 입력 로직
    private fun askStartReservation(): Boolean = executeWithRetry { inputView.startMessage() }

    private fun askAddMore(): Boolean = executeWithRetry { inputView.addMoreMovie() }

    // 유틸
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
