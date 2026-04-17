package console.controller

import console.view.InputView
import console.view.OutputView
import model.cart.Cart
import model.cart.CartItem
import model.discount.PaymentMethod
import model.discount.payDiscountPolicy.PayDiscountBenefits
import model.discount.payDiscountPolicy.PaymentPayDiscountPolicy
import model.discount.payDiscountPolicy.PointPayDiscountPolicy
import model.discount.reserveDiscountPolicy.MovieDayDiscountPolicy
import model.discount.reserveDiscountPolicy.MovieDiscountPolicy
import model.discount.reserveDiscountPolicy.TimeDiscountPolicy
import model.movie.Movie
import model.schedule.Screening
import repository.MovieRepository
import repository.ReservationRepository
import repository.ScreeningRepository
import java.time.LocalDate

class MovieController {
    val inputView = InputView()
    val outputView = OutputView()

    val screeningRepository = ScreeningRepository()
    val reservationRepository = ReservationRepository(screeningRepository)
    val movieRepository = MovieRepository()
    var cart = Cart()

    fun checkMovieReserve(): Boolean {
        return try {
            val answer = inputView.movieReserveInput()

            return answer
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            checkMovieReserve()
        }
    }

    fun checkMovieAdd(): Boolean =
        try {
            val answer = inputView.againMovieReserveInput()

            return answer
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            checkMovieAdd()
        }

    fun searchMovie(): Movie =
        try {
            val input = inputView.movieTitleInput()
            movieRepository.findByTitle(input)
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            searchMovie()
        }

    fun inputDate(): LocalDate {
        return try {
            val date = inputView.dateInput()

            return date
        } catch (e: Exception) {
            outputView.printErrorMessage(e.message.toString())
            inputDate()
        }
    }

    fun getScreeningsOfDateAndTitle(): Screening {
        val movie = searchMovie()
        var screenings: List<Screening>

        return try {
            val date = inputDate()
            screenings = screeningRepository.findByMovieAndDate(movie = movie, date = date)
            if (screenings.isEmpty()) throw IllegalArgumentException("해당 날짜에 상영 중인 영화가 없습니다.")
            outputView.printScreenings(screenings)
            return selectMovieTime(cart, screenings)
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            getScreeningsOfDateAndTitle()
        }
    }

    fun selectMovieTime(
        cart: Cart,
        screenings: List<Screening>,
    ): Screening {
        var selectedScreening: Screening

        return try {
            val number = inputView.screeningNumberInput(screenings.size)
            selectedScreening = screenings[number - 1]

            cart.parseOverlapping(selectedScreening)

            selectedScreening
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            selectMovieTime(cart, screenings)
        }
    }

    fun inputSeats(): List<String> {
        return try {
            return inputView
                .reserveSeatsInput()
                .split(",")
                .map { it.trim() }
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            inputSeats()
        }
    }

    fun reservedScreening(selectedScreening: Screening): CartItem {
        var seatNames: List<String>
        var reservedScreening: Screening
        while (true) {
            try {
                seatNames = inputSeats()
                reservedScreening = getReservedScreening(selectedScreening, seatNames)
                break
            } catch (e: IllegalArgumentException) {
                outputView.printErrorMessage(e.message.toString())
            }
        }

        return CartItem(
            reservedScreening,
            seatNames,
        )
    }

    fun getReservedScreening(
        selectedScreening: Screening,
        seatNames: List<String>,
    ): Screening = selectedScreening.reserveSeats(seatNames)

    fun usePoint(): Int =
        try {
            inputView.pointInput().toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요")
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            usePoint()
        }

    fun selectPaymentMethod(): PaymentMethod =
        try {
            return inputView.paymentMethodInput()
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            selectPaymentMethod()
        }

    fun checkPayment(): Boolean {
        return try {
            val answer = inputView.paymentConfirmInput()

            return answer
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            checkPayment()
        }
    }

    fun run() {
        if (!checkMovieReserve()) return

        do {
            val selectedScreening: Screening = getScreeningsOfDateAndTitle()

            outputView.printSeatInventory(selectedScreening.seatInventory)

            val cartItem = reservedScreening(selectedScreening)
            cart = cart.addItem(cartItem)

            outputView.printCartItemAdded(cartItem)
        } while (checkMovieAdd())

        outputView.printCart(cart)

        val usePoint = usePoint()
        val paymentMethod = selectPaymentMethod()

        val moviePrice =
            cart.calculateItemsPrice(
                reserveDiscountPolicy =
                    MovieDiscountPolicy(
                        movieDiscountPolicies =
                            listOf(
                                MovieDayDiscountPolicy(),
                                TimeDiscountPolicy(),
                            ),
                    ),
            )

        val totalPrice =
            PayDiscountBenefits(
                payDiscountPolicies =
                    listOf(
                        PointPayDiscountPolicy(usePoint),
                        PaymentPayDiscountPolicy(paymentMethod),
                    ),
            ).calculatePrice(moviePrice)

        outputView.printTotalPrice(totalPrice.value)

        if (checkPayment()) {
            reservationRepository.save(
                cart = cart,
                paymentMethod = paymentMethod,
                usedPoint = usePoint,
                totalPrice = totalPrice.value,
            )
            outputView.printReservationComplete(cart, totalPrice.value, usePoint)
        }
    }
}
