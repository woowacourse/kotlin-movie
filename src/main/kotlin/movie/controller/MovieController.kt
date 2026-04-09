package movie.controller

import movie.domain.Point
import movie.domain.Price
import movie.domain.discount.DiscountPolicy
import movie.domain.movie.Movie
import movie.domain.movie.MovieTime
import movie.domain.movie.MovieTitle
import movie.domain.movie.ScreeningMovie
import movie.domain.movie.ScreeningMovies
import movie.domain.movie.Theater
import movie.domain.movie.TheaterScheduler
import movie.domain.movie.Theaters
import movie.domain.movie.Ticket
import movie.domain.payment.Card
import movie.domain.payment.Cash
import movie.domain.payment.Payment
import movie.domain.payment.PaymentMethod
import movie.domain.point.PointPolicy
import movie.domain.seat.number.SeatNumber
import movie.view.InputParser
import movie.view.InputValidator
import movie.view.InputView
import movie.view.OutputView
import java.time.LocalDate
import java.time.LocalTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MovieController {
    private val movies = listOf(
        Movie(title = MovieTitle("F1 더 무비")),
        Movie(title = MovieTitle("토이 스토리")),
        Movie(title = MovieTitle("아이언맨")),
    )
    private val movieTime =
        MovieTime(
            date = LocalDate.of(2026, 4, 9),
            startTime = LocalTime.of(12, 0, 0),
            endTime = LocalTime.of(14, 30, 0),
        )
    private val theaterList =
        listOf(
            Theater(
                openTime = LocalTime.of(12, 0, 0),
                closeTime = LocalTime.of(23, 59, 59),
            ),
            Theater(
                openTime = LocalTime.of(7, 0, 0),
                closeTime = LocalTime.of(23, 59, 59),
            ),
        )

    private val screeningMovies =
        ScreeningMovies(
            listOf(
                ScreeningMovie(
                    movie = movies[0],
                    movieTime =
                        MovieTime(
                            date = LocalDate.of(2025, 9, 20),
                            startTime = LocalTime.of(10, 20, 0),
                            endTime = LocalTime.of(12, 50, 0),
                        ),
                    theater = theaterList[1],
                ),
                ScreeningMovie(
                    movie = movies[0],
                    movieTime =
                        MovieTime(
                            date = LocalDate.of(2025, 9, 20),
                            startTime = LocalTime.of(13, 0, 0),
                            endTime = LocalTime.of(15, 30, 0),
                        ),
                    theater = theaterList[0],
                ),
                ScreeningMovie(
                    movie = movie,
                    movieTime =
                        MovieTime(
                            date = LocalDate.of(2026, 4, 9),
                            startTime = LocalTime.of(20, 30, 0),
                            endTime = LocalTime.of(23, 30, 0),
                        ),
                    theater = theaterList[1],
                ),
            ),
        )

    private val theaters: Theaters =
        Theaters(
            theaters = theaterList,
        )

    private val scheduler: TheaterScheduler =
        TheaterScheduler(
            theaters = theaters,
            screeningMovies = screeningMovies,
        )

    private val discountPolicy = DiscountPolicy()
    private val payment = Payment()
    private val pointPolicy = PointPolicy()

    fun run() {
        val isStart = getReservationStart()

        if (!isStart) {
            return
        }

        val ticket = Ticket()

        while (true) {
            val movieTitle = getMovieTitle()
            val movieTimes = getMovieTimes(title = movieTitle)
            OutputView.printMovieStartTimes(movieTimes)
            val screeningMovie = getScreeningMovie(movieTimes, ticket)

            OutputView.printSeats(screeningMovie = screeningMovie)
            val selectedSeatNumbers = getSeatNumbers(screeningMovie = screeningMovie)

            val reservation = ticket.addReservation(
                screeningMovie = screeningMovie,
                seatNumbers = selectedSeatNumbers
            )

            OutputView.printReservationAddMessage(reservation = reservation)

            if (!getContinueReservation()) {
                break
            }
        }

        OutputView.printCart(ticket = ticket)

        val reservations = ticket.getReservations()
        var totalPrice = Price(0)
        reservations.forEach {
            val price = it.getTotalPrice()
            val discountPrice =
                discountPolicy.calculateDiscount(
                    totalPrice = price,
                    movieTime = it.screeningMovie.movieTime,
                )
            totalPrice = totalPrice.sumPrice(targetPrice = discountPrice)
        }

        val point = getUsePoint()
        val paymentMethod = getPaymentMethod()

        val pointUsedPrice = pointPolicy.usePoint(totalPrice = totalPrice, usePoint = point)
        val paymentPrice = payment.paymentPrice(method = paymentMethod, totalPrice = pointUsedPrice)

        OutputView.printTotalPrice(totalPrice = paymentPrice)

        val isPayment = getUserPayment()

        if (isPayment) {
            OutputView.printReceipt(
                ticket = ticket,
                paymentPrice = paymentPrice,
                usePoint = point,
            )

            OutputView.printThankYou()
        }
    }

    fun getMovieTimes(title: MovieTitle): List<ScreeningMovie> =
        whileGetInput {
            val date = getDate()

            scheduler.getMovies(title = title, date = date)
        }

    fun getReservationStart(): Boolean =
        whileGetInput {
            val input = InputView.readReservationStart()
            InputValidator.validateYesNo(input)

            InputParser.parseYesNo(input)
        }

    fun getMovieTitle(): MovieTitle =
        whileGetInput {
            val input = InputView.readMovieTitle()

            val movieTitle = InputParser.parseMovieTitle(input)

            if (!scheduler.containsMovieTitle(movieTitle)) {
                throw IllegalArgumentException("상영 중인 영화가 아닙니다.")
            }

            movieTitle
        }

    fun getDate(): LocalDate =
        whileGetInput {
            val input = InputView.readMovieDate()
            InputValidator.validateDate(input)

            InputParser.parseDate(input)
        }

    fun getScreeningMovie(
        screeningMovies: List<ScreeningMovie>,
        ticket: Ticket,
    ): ScreeningMovie =
        whileGetInput {
            val input = InputView.readSelectedMovieTimeNumber()
            InputValidator.validateNumber(input)

            val index = InputParser.parseIndex(input = input, size = screeningMovies.size)
            val selectedMovie = screeningMovies[index]

            if (ticket.isDupTime(selectedMovie.movieTime)) {
                throw IllegalArgumentException("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
            }

            selectedMovie
        }

    fun getSeatNumbers(screeningMovie: ScreeningMovie): List<SeatNumber> =
        whileGetInput {
            val input = InputView.readReservationSeat()
            InputValidator.validateSeatNumbers(input)

            val seatNumbers = InputParser.parseSeatNumbers(input)

            if (screeningMovie.isAbleReservation(seatNumbers)) {
                throw IllegalArgumentException("이미 예약된 좌석입니다.")
            }

            seatNumbers
        }

    fun getContinueReservation(): Boolean =
        whileGetInput {
            val input = InputView.readContinueReservation()
            InputValidator.validateYesNo(input)

            InputParser.parseYesNo(input)
        }

    fun getUsePoint(): Point =
        whileGetInput {
            val input = InputView.readUsePoint()
            InputValidator.validateNumber(input)

            InputParser.parsePoint(input)
        }

    fun getPaymentMethod(): PaymentMethod =
        whileGetInput {
            val paymentMethods = listOf(Cash(), Card())
            val input = InputView.readPaymentType(paymentMethods)
            InputValidator.validateNumber(input)

            val index = InputParser.parseIndex(input = input, size = paymentMethods.size)

            paymentMethods[index]
        }

    fun getUserPayment(): Boolean =
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
