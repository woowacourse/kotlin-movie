package controller

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
import repository.CinemaRepository
import view.InputView
import view.OutputView
import java.lang.Exception
import java.time.LocalDate

class CinemaController(
    private val repository: CinemaRepository,
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
            throw IllegalArgumentException("날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)")
        }

    private fun findAvailableScreenings(
        title: String,
        date: LocalDate,
    ): List<Screening> {
        val screenings = repository.findByMovieTitleAndDate(title, date)
        require(screenings.isNotEmpty()) { "해당 조건의 상영이 없습니다." }
        return screenings
    }

    private fun readAvailableScreening(availableScreenings: List<Screening>): Screening =
        retryPrompt {
            outputView.printScreenings(availableScreenings)

            val selectedNumber = inputView.readScreeningNumber()
            require(selectedNumber in 1..availableScreenings.size) {
                "올바른 상영 번호를 선택해 주세요."
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
                "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요."
            }
        }
    }

    private fun readSeats(): List<Seat> {
        val input = inputView.readSeatNumbers()
        require(!input.isNullOrBlank()) { "올바른 좌석 번호를 입력해주세요." }
        val seatNumbers =
            input
                .split(",")
                .map { it.trim().uppercase() }
                .filter { it.isNotBlank() }

        require(seatNumbers.toSet().size == seatNumbers.size) {
            "동일 좌석을 중복 예약할 수 없습니다."
        }

        return allSeats.findAllBySeatNumbers(seatNumbers)
    }

    private fun validateSeatsNotReserved(
        screening: Screening,
        seats: List<Seat>,
    ) {
        require(seats.none { screening.isReserved(it) }) {
            "이미 예약된 좌석은 다시 선택할 수 없습니다."
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
            repository.screenings.firstOrNull {
                it.movie == screening.movie && it.startTime == screening.startTime
            } ?: screening

        return allSeats.allSeats().filter { sameScreen.isReserved(it) }
    }

    private fun updateScreeningReservation(
        screening: Screening,
        selectedSeats: List<Seat>,
    ) {
        repository.updateScreening(
            repository.screenings.map {
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
}
