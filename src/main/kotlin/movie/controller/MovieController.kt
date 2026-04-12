package movie.controller

import movie.domain.Cart
import movie.domain.MovieManager
import movie.domain.PaymentManager
import movie.domain.Schedule
import movie.domain.payment.Card
import movie.domain.payment.Cash
import movie.domain.payment.PaymentMethod
import movie.domain.point.Point
import movie.domain.seat.SeatNumber
import movie.view.InputParser
import movie.view.InputValidator
import movie.view.InputView
import movie.view.OutputView
import java.time.LocalDate
import java.time.LocalDateTime

class MovieController(
    private val movieManager: MovieManager,
    private val paymentManager: PaymentManager = PaymentManager(),
    private val cart: Cart = Cart(),
) {
    fun run() {
        if (!getReservationStart()) {
            return
        }

        while (true) {
            movieReservationStart()

            if (!getContinueReservation()) {
                break
            }
        }

        moviePricePayment()
    }

    private fun moviePricePayment() {
        OutputView.printCart(cart)

        val usePoint = getUsePoint()
        val paymentMethod = getPaymentMethod()

        val paymentPrice = paymentManager.calculateFinalPrice(
            cart = cart,
            usePoint = usePoint,
            paymentMethod = paymentMethod,
        )

        OutputView.printTotalPrice(paymentPrice)

        if (getUserPayment()) {
            OutputView.printReceipt(cart = cart, paymentPrice = paymentPrice, usePoint = usePoint)
            OutputView.printThankYou()
        }
    }

    private fun movieReservationStart() {
        val title = getTitle()
        val movieTimes = getMovieTimes(title)

        val schedule = getSchedule(title = title, movieTimes = movieTimes)
        val seats = getSeats(schedule)

        schedule.addSeats(seats)
        cart.addReservation(schedule = schedule, seats = seats)

        OutputView.printReservationAddMessage(schedule, seats)
    }

    private fun getSeats(schedule: Schedule): List<SeatNumber> {
        return whileGetInput {
            val input = InputView.readReservationSeat()
            InputValidator.validateSeatNumbers(input)

            val seats = InputParser.parseSeatNumbers(input)
            require(!schedule.isReservationSeats(seats)) { "이미 예약된 좌석입니다." }

            seats
        }
    }

    private fun getMovieTimes(title: String): List<LocalDateTime> {
        return whileGetInput {
            val date = getDate()
            val movieTimes = movieManager.getMovieStartTime(title, date)

            movieTimes
        }
    }

    private fun getSchedule(title: String, movieTimes: List<LocalDateTime>): Schedule {
        OutputView.printMovieStartTimes(movieTimes)

        return whileGetInput {
            val input = InputView.readSelectedMovieTimeNumber()
            InputValidator.validateNumber(input)
            val index = InputParser.parseIndex(input = input, size = movieTimes.size)

            val schedule = movieManager.getSchedule(title = title, startTime = movieTimes[index])

            require(!cart.isDuplicateTime(schedule)) {
                "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요."
            }

            OutputView.printSeats(schedule)

            schedule
        }
    }

    private fun getDate(): LocalDate {
        return whileGetInput {
            val date = InputView.readMovieDate()

            InputValidator.validateDate(date)

            InputParser.parseDate(date)
        }
    }

    private fun getTitle(): String {
        return whileGetInput {
            val title = InputView.readMovieTitle()

            require(movieManager.hasMovieTitle(title)) { "상영 중인 영화가 없습니다." }

            title
        }
    }

    private fun getReservationStart(): Boolean =
        whileGetInput {
            val input = InputView.readReservationStart()
            InputValidator.validateYesNo(input)

            InputParser.parseYesNo(input)
        }

    private fun getContinueReservation(): Boolean =
        whileGetInput {
            val input = InputView.readContinueReservation()
            InputValidator.validateYesNo(input)

            InputParser.parseYesNo(input)
        }

    private fun getUsePoint(): Point =
        whileGetInput {
            val input = InputView.readUsePoint()
            InputValidator.validateNumber(input)

            InputParser.parsePoint(input)
        }

    private fun getPaymentMethod(): PaymentMethod =
        whileGetInput {
            val paymentMethods = listOf(Cash(), Card())
            val input = InputView.readPaymentType(paymentMethods)
            InputValidator.validateNumber(input)

            val index = InputParser.parseIndex(input = input, size = paymentMethods.size)

            paymentMethods[index]
        }

    private fun getUserPayment(): Boolean =
        whileGetInput {
            val input = InputView.readUserPayment()
            InputValidator.validateYesNo(input)

            InputParser.parseYesNo(input)
        }

    private fun <T> whileGetInput(action: () -> T): T {
        while (true) {
            try {
                val input = action()

                return input
            } catch (e: IllegalArgumentException) {
                OutputView.printErrorMessage(e.message)
            }
        }
    }
}
