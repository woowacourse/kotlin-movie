package controller

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
import model.movie.Movies
import model.schedule.Schedule
import model.schedule.Screening
import view.InputView
import view.OutputView
import java.time.LocalDate
import java.time.LocalTime

class MovieController {
    val inputView = InputView()
    val outputView = OutputView()
    val movies = Movies.createMovies()

    val selectedScreenings = mutableListOf<Screening>()

    var schedule =
        Schedule(
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(23, 59),
            screenings = Schedule.screenings,
        )

    // 시작 시 실행 여부를 받는 함수
    fun checkMovieReserve(): Boolean {
        return try {
            val input = inputView.movieReserveInput()

            return when (input) {
                "Y" -> true
                "N" -> false
                else -> throw IllegalArgumentException("잘못된 입력입니다")
            }
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            checkMovieReserve()
        }
    }

    // 예약을 한 번 이상 마친 후에 다시 예약을 할 것인지 여부를 묻는 함수
    fun checkMovieAdd(): Boolean =
        try {
            val input = inputView.againMovieReserveInput()
            when (input) {
                "Y" -> true
                "N" -> false
                else -> throw IllegalArgumentException("잘못된 입력입니다")
            }
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            checkMovieAdd()
        }

    // 제목과 일치하는 영화 리스트를 반환하는 함수
    fun searchMovie(): Movie =
        try {
            val input = inputView.movieTitleInput()
            movies.findMovie(input)
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            searchMovie()
        }

    // 날짜를 받아 LocalDate로 반환해주는 함수
    fun inputDate(): LocalDate {
        val dateRegex = """^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$""".toRegex()
        return try {
            val input = inputView.dateInput()
            if (!input.matches(dateRegex)) {
                throw IllegalArgumentException("잘못된 날짜 입력입니다")
            }
            LocalDate.parse(input)
        } catch (e: Exception) {
            outputView.printErrorMessage(e.message.toString())
            inputDate()
        }
    }

    fun getScreeningsOfDateAndTitle(): Screening {
        // 영화 선택
        val movie = searchMovie()
        var screenings: List<Screening>

        while (true) {
            // 날짜 선택 후 해당 영화의 상영 목록 조회
            val date = inputDate()
            screenings = getScreenings(movie = movie, date = date)
            if (screenings.isNotEmpty()) {
                return selectMovieTime(screenings)
            }
        }
    }

    // 상영관을 받아오는 함수
    fun getScreenings(
        movie: Movie,
        date: LocalDate,
    ): List<Screening> {
        val screenings =
            schedule
                .getScreeningsByMovie(movie)
                .filter { it.startDateTime.toLocalDate() == date }

        if (screenings.isEmpty()) {
            outputView.printErrorMessage("해당 날짜에 상영 중인 영화가 없습니다.")
        } else {
            outputView.printScreenings(screenings)
        }
        return screenings
    }

    // 상영 시간대를 고르는 함수
    fun selectMovieTime(screenings: List<Screening>): Screening {
        var selectedScreening: Screening

        return try {
            val number = inputView.screeningNumberInput(screenings.size)
            selectedScreening = screenings[number - 1]

            if (selectedScreenings.any {
                    it.movie.movieTitle != selectedScreening.movie.movieTitle &&
                        it.isOverlapping(selectedScreening)
                }
            ) {
                throw IllegalArgumentException("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
            }

            selectedScreening
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            selectMovieTime(screenings)
        }
    }

    // 좌석을 선택하는 함수
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
                // 입력한 좌석
                seatNames = inputSeats()
                // 장바구니에 추가
                reservedScreening = getReservedScreening(selectedScreening, seatNames)
                break
            } catch (e: IllegalArgumentException) {
                outputView.printErrorMessage(e.message.toString())
            }
        }
        schedule =
            schedule.updateScreening(
                selectedScreening,
                reservedScreening,
            )

        return CartItem(
            reservedScreening,
            seatNames,
        )
    }

    // 장바구니에 추가
    fun getReservedScreening(
        selectedScreening: Screening,
        seatNames: List<String>,
    ): Screening = selectedScreening.reserveSeats(seatNames)

    // 포인트를 받아오는 함수
    fun usePoint(): Int =
        try {
            inputView.pointInput().toIntOrNull() ?: throw IllegalArgumentException("숫자를 입력해주세요")
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            usePoint()
        }

    // 결제 방법을 선택하는 함수
    fun selectPaymentMethod(): PaymentMethod =
        try {
            val input = inputView.paymentMethodInput()
            when (input) {
                "1" -> PaymentMethod.CARD
                "2" -> PaymentMethod.CASH
                else -> throw IllegalArgumentException("잘못된 입력입니다")
            }
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            selectPaymentMethod()
        }

    // 결제할 것인지 여부를 묻는 함수
    fun checkPayment(): Boolean {
        return try {
            val input = inputView.paymentConfirmInput()

            return when (input) {
                "Y" -> true
                "N" -> false
                else -> throw IllegalArgumentException("잘못된 입력입니다")
            }
        } catch (e: IllegalArgumentException) {
            outputView.printErrorMessage(e.message.toString())
            checkPayment()
        }
    }

    // 장바구니 안의 값들을 계산하는 함수
    fun run() {
        if (!checkMovieReserve()) return
        var cart = Cart()

        do {
            // 제목과 날짜를 받아 영화 리스트를 받아온다
            val selectedScreening: Screening = getScreeningsOfDateAndTitle()
            selectedScreenings.add(selectedScreening)

            // 좌석 배치도 출력 후 좌석 선택
            outputView.printSeatInventory(selectedScreening.seatInventory)

            // 좌석을 고른 후에 장바구니에 담는다
            val cartItem = reservedScreening(selectedScreening)
            cart = cart.addItem(cartItem)

            outputView.printCartItemAdded(cartItem)
        } while (checkMovieAdd())

        // 장바구니 출력
        outputView.printCart(cart)

        // 포인트 및 결제 수단 선택
        val usePoint = usePoint()
        val paymentMethod = selectPaymentMethod()

        // 영화 시간에 따른 가격 계산
        val moviePrice =
            cart.calculateItemsPrice(
                reserveDiscountPolicy =
                    MovieDiscountPolicy(
                        movieDiscountPolicies =
                            listOf(
                                MovieDayDiscountPolicy(), // 영화 할인 정책
                                TimeDiscountPolicy(), // 시간 할인 정책
                            ),
                    ),
            )

        // 포인트 및 현금, 카드 할인 계산
        val totalPrice =
            PayDiscountBenefits(
                payDiscountPolicies =
                    listOf(
                        PointPayDiscountPolicy(usePoint),
                        PaymentPayDiscountPolicy(paymentMethod),
                    ),
            ).calculatePrice(moviePrice)

        outputView.printTotalPrice(totalPrice.value)

        // 결제 확인
        if (checkPayment()) {
            outputView.printReservationComplete(
                cart,
                totalPrice.value,
                usePoint,
            )
        }
    }
}
