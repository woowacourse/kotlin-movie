package movie.controller

import movie.data.MovieData
import movie.domain.payment.PaymentResult
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
import movie.domain.movie.Movie
import movie.domain.movie.Movies
import movie.domain.payment.Cash
import movie.domain.payment.CreditCard
import movie.domain.payment.PaymentMethod
import movie.domain.payment.PriceCalculator
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatRow
import movie.domain.seat.SelectedSeats
import movie.domain.user.User
import movie.view.InputView
import movie.view.OutputView
import java.time.LocalDate

class MovieController(
    private val inputView: InputView = InputView(),
    private val outputView: OutputView = OutputView(),
    private val movies: Movies = MovieData.createMovies(),
    private val user: User = MovieData.createUser(),
    private val priceCalculator: PriceCalculator =
        PriceCalculator(
            DiscountPolicies(
                listOf(MovieDayDiscount()),
                listOf(TimeDiscount()),
            ),
        ),
) {
    fun run() {
        if (!askStartReservation()) return

        val reservations = collectReservations()

        showCart(reservations)

        val paymentResult = processPayment(reservations)

        confirmAndComplete(reservations, paymentResult)
    }

    // 메인 로직
    private fun processPayment(reservations: Reservations): PaymentResult {
        val point = inputPoint()
        val paymentMethod = selectPaymentMethod()

        val paymentResult = priceCalculator.calculate(reservations, point, paymentMethod)

        outputView.printFinalPrice(paymentResult.totalPrice)
        return paymentResult
    }

    private fun confirmAndComplete(
        reservations: Reservations,
        paymentResult: PaymentResult,
    ) {
        val confirm = executeWithRetry { inputView.confirmPayment() }

        if (confirm) {
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
            require(input <= user.point.value) { "보유 포인트를 초과할 수 없습니다." }
            Point(input)
        }

    private fun selectPaymentMethod(): PaymentMethod {
        val input = executeWithRetry { inputView.inputPayment() }

        return when (input) {
            1 -> CreditCard()
            2 -> Cash()
            else -> throw IllegalArgumentException("유효하지 않은 결제 수단입니다.")
        }
    }

    // 예매 로직
    private fun collectReservations(): Reservations {
        var reservations = Reservations(emptyList())
        do {
            reservations = addReservation(reservations)
        } while (askAddMore())

        return reservations
    }

    private fun addReservation(existingReservations: Reservations): Reservations =
        executeWithRetry {
            val reservation = selectMovieAndSeats()
            val updatedReservations = existingReservations.add(reservation)
            outputView.printAddedToCart(reservation)
            updatedReservations
        }

    private fun selectMovieAndSeats(): Reservation {
        val movie = selectMovie()
        val date = selectDate(movie)
        val screening = selectScreening(movie, date)
        val seats = selectSeats(screening)
        val reservedScreening = screening.reserve(seats)
        return reservedScreening.createReservation(seats)
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
    ): Screening =
        executeWithRetry {
            val screenings = movie.getScreeningsByDate(date)
            outputView.printScreeningList(screenings)

            val number = inputView.inputScreeningNumber()
            val selectedScreening = screenings.findByNumber(number)

            selectedScreening
        }

    private fun selectSeats(screening: Screening): SelectedSeats =
        executeWithRetry {
            outputView.printSeatLayout(screening.slot.screen, screening.reservatedSeats)
            val input = inputView.inputSeat()
            val seats = parseSeatInput(input, screening.slot.screen)
            val reserveAvailableSeats = screening.isReserveAvailable(seats)
            reserveAvailableSeats
        }

    private fun parseSeatInput(
        input: String,
        screen: Screen,
    ): SelectedSeats =
        SelectedSeats(
            input
                .split(",")
                .map { it.trim() }
                .map { seatInput ->
                    val row = SeatRow(seatInput.substring(0, 1).uppercase())
                    val column =
                        seatInput.substring(1).toIntOrNull()
                            ?: throw IllegalArgumentException("유효하지 않은 좌석 형식입니다: $seatInput")
                    screen.seats.findSeat(row, SeatColumn(column))
                },
        )

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
