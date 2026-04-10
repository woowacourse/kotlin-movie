package controller

import constants.ErrorMessages
import domain.account.Account
import domain.payment.DiscountPolicy
import domain.payment.PayResult
import domain.payment.Payment
import domain.payment.PaymentMethod
import domain.reservation.Cart
import domain.reservation.ReservedScreen
import domain.reservation.Seat
import domain.reservation.Seats
import domain.screening.Screening
import repository.Screenings
import view.InputView
import view.OutputView
import java.lang.Exception
import java.time.LocalDate

class CinemaController(
    private val screenings: Screenings,
    private val inputView: InputView,
    private val outputView: OutputView,
    private val account: Account = Account(),
    private val allSeats: Seats = Seats.create(),
) {
    private var cart: Cart = Cart()

    fun run() {
        if (!isReservationStarted()) {
            outputView.printEndTicketing()
            return
        }

        reserveMovies()
        proceedPayment()
    }

    private fun isReservationStarted(): Boolean = inputView.readConfirmTicketingStart().uppercase() == CONFIRM_INPUT

    private fun reserveMovies() {
        do {
            retryPrompt { reserveOneMovie() }
        } while (askToAddMoreMovie())
    }

    private fun askToAddMoreMovie(): Boolean = inputView.readConfirmAddOtherMovie().uppercase() == CONFIRM_INPUT

    private fun reserveOneMovie() {
        val title = inputView.readMovieTitle()
        val date = readDate()
        val availableScreenings = findAvailableScreenings(title, date)
        val selectedScreening = readAvailableScreening(availableScreenings)
        val selectedSeats = retryPrompt { readAvailableSeats(selectedScreening) }

        val reservedItem =
            ReservedScreen(
                screen = selectedScreening,
                seats = selectedSeats,
            )

        addToCart(reservedItem)
        outputView.printCartAdded(reservedItem)
    }

    private fun readDate(): LocalDate =
        try {
            LocalDate.parse(inputView.readDate())
        } catch (e: Exception) {
            throw IllegalArgumentException(ErrorMessages.INVALID_DATE_FORMAT.message)
        }

    private fun findAvailableScreenings(
        title: String,
        date: LocalDate,
    ): List<Screening> {
        val screenings = screenings.findByMovieTitleAndDate(title, date)
        require(screenings.isNotEmpty()) { ErrorMessages.SCREENING_DOES_NOT_EXIST.message }
        return screenings
    }

    private fun readAvailableScreening(availableScreenings: List<Screening>): Screening =
        retryPrompt {
            outputView.printScreenings(availableScreenings)

            val selectedNumber = inputView.readScreeningNumber()
            require(selectedNumber in 1..availableScreenings.size) {
                ErrorMessages.INCORRECT_SCREENING_NUMBER.message
            }

            val selectedScreening = availableScreenings[selectedNumber - 1]
            validateScreeningOverlap(selectedScreening)
            selectedScreening
        }

    private fun readAvailableSeats(screening: Screening): List<Seat> {
        outputView.printSeatLayout(allSeats, selectedScreeningReservedSeats(screening))

        val selectedSeats = readSeats()
        validateSeatsNotReserved(screening, selectedSeats)
        return selectedSeats
    }

    private fun addToCart(reservedItem: ReservedScreen) {
        cart = cart.add(reservedItem)
        updateScreeningReservation(reservedItem.screen, reservedItem.seats)
    }

    private fun proceedPayment() {
        outputView.printCart(cart)

        val point = retryPrompt { inputView.readPointAmount() }
        val paymentMethod = retryPrompt { PaymentMethod.validate(inputView.readPaymentMethod()) }
        val payment = Payment(cart, DiscountPolicy())

        when (val result = payment.pay(point, account, paymentMethod)) {
            is PayResult.Success -> confirmPayment(result)
            is PayResult.Failure -> outputView.printErrorMessage(result.message)
        }
    }

    private fun confirmPayment(result: PayResult.Success) {
        outputView.printTotalCost(result.paidAmount)

        val confirm = inputView.readConfirmPay().uppercase()
        if (confirm == CONFIRM_INPUT) {
            printReservationHistory(result)
            return
        }

        outputView.printCancelPay()
    }

    private fun validateScreeningOverlap(target: Screening) {
        cart.items.forEach {
            require(!it.screen.overlaps(target)) {
                ErrorMessages.OVERLAP_MOVIE_TIME.message
            }
        }
    }

    private fun readSeats(): List<Seat> {
        val input = inputView.readSeatNumbers()
        require(input.isNotBlank()) { ErrorMessages.INCORRECT_SEAT_NUMBER.message }
        val seatNumbers =
            input
                .split(SEAT_NUMBER_PARSER)
                .map { it.trim().uppercase() }
                .filter { it.isNotBlank() }

        require(seatNumbers.toSet().size == seatNumbers.size) {
            ErrorMessages.SELECT_SAME_SEAT.message
        }

        return allSeats.findAllBySeatNumbers(seatNumbers)
    }

    private fun validateSeatsNotReserved(
        screening: Screening,
        seats: List<Seat>,
    ) {
        require(seats.none { screening.isReserved(it) }) {
            ErrorMessages.SELECTED_RESERVED_SEAT.message
        }
    }

    private fun printReservationHistory(result: PayResult.Success) {
        outputView.printFinishReservationMessage()

        result.cart.items.forEach {
            outputView.printTicketReservationInformation(it.seats, it.screen)
        }

        outputView.printPaymentResult(result.paidAmount, result.usedPoint)
    }

    private fun selectedScreeningReservedSeats(screening: Screening): List<Seat> {
        val sameScreen =
            this@CinemaController.screenings.screenings.firstOrNull {
                it.movie == screening.movie && it.startTime == screening.startTime
            } ?: screening

        return allSeats.allSeats().filter { sameScreen.isReserved(it) }
    }

    private fun updateScreeningReservation(
        screening: Screening,
        selectedSeats: List<Seat>,
    ) {
        screenings.updateScreening(
            this@CinemaController.screenings.screenings.map {
                if (it.movie == screening.movie && it.startTime == screening.startTime) {
                    it.reserve(selectedSeats)
                } else {
                    it
                }
            },
        )
    }

    private fun <T> retryPrompt(action: () -> T): T {
        while (true) {
            try {
                return action()
            } catch (e: Exception) {
                outputView.printErrorMessage(e.message ?: "")
            }
        }
    }

    companion object {
        private const val CONFIRM_INPUT = "Y"
        private const val SEAT_NUMBER_PARSER = ","
    }
}
