package movie.infrastructure.db

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
import movie.domain.payment.Cash
import movie.domain.payment.CreditCard
import movie.domain.payment.PriceCalculator
import movie.domain.reservation.Reservations
import movie.domain.seat.Seat
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatGrade
import movie.domain.seat.SeatRow
import movie.domain.seat.SelectedSeats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Connection
import java.time.LocalDate

class IntegrationTest {
    private lateinit var connection: Connection
    private lateinit var reservedSeatRepository: JdbcReservedSeatRepository
    private lateinit var screeningRepository: JdbcScreeningRepository
    private lateinit var movieRepository: JdbcMovieRepository
    private lateinit var reservationRepository: JdbcReservationRepository

    @BeforeEach
    fun setUp() {
        connection = DatabaseConnector.connectTest()
        DatabaseInitializer(connection).initialize()
        reservedSeatRepository = JdbcReservedSeatRepository(connection)
        screeningRepository = JdbcScreeningRepository(connection, reservedSeatRepository)
        movieRepository = JdbcMovieRepository(connection, screeningRepository)
        reservationRepository = JdbcReservationRepository(connection, reservedSeatRepository)
    }

    @AfterEach
    fun tearDown() {
        connection.createStatement().use { it.execute("DROP ALL OBJECTS") }
        connection.close()
    }

    @Test
    fun `DB에서 영화를 조회하고 예매하고 결제까지 전체 흐름이 동작한다`() {
        val movies = movieRepository.findAll()
        val movie = movies.findMovie("F1 더 무비")
        assertThat(movie.title.toString()).isEqualTo("F1 더 무비")

        val date = LocalDate.of(2025, 9, 20)
        assertThat(movie.hasScreeningOnDate(date)).isTrue()
        val screenings = movie.getScreeningsByDate(date)

        val screening = screenings.findByNumber(1)

        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
                    Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B),
                ),
            )
        screening.isReserveAvailable(selectedSeats)

        val reservedScreening = screening.reserve(selectedSeats)
        val reservation = reservedScreening.createReservation(selectedSeats)
        val reservations = Reservations(listOf(reservation))

        val priceCalculator =
            PriceCalculator(
                DiscountPolicies(
                    listOf(MovieDayDiscount()),
                    listOf(TimeDiscount()),
                ),
            )
        val paymentResult =
            priceCalculator.calculate(
                reservations,
                Point(1000),
                CreditCard(),
            )

        val reservationId =
            reservationRepository.save(
                reservations,
                paymentResult.totalPrice,
                paymentResult.usedPoint,
                paymentResult.paymentMethodName(),
            )
        assertThat(reservationId).isGreaterThan(0L)

        val updatedMovies = movieRepository.findAll()
        val updatedMovie = updatedMovies.findMovie("F1 더 무비")
        val updatedScreenings = updatedMovie.getScreeningsByDate(date)
        val updatedScreening = updatedScreenings.findByNumber(1)

        val seatA1 = Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B)
        assertThat(updatedScreening.isSeatAvailable(seatA1)).isFalse()
    }

    @Test
    fun `이미 예약된 좌석은 재예약할 수 없다`() {
        // 1. screening 102 (F1 13:00) 에는 B2가 이미 예약됨
        val movies = movieRepository.findAll()
        val movie = movies.findMovie("F1 더 무비")
        val screenings = movie.getScreeningsByDate(LocalDate.of(2025, 9, 20))
        val screening = screenings.findByNumber(2) // 13:00

        val selectedSeats =
            SelectedSeats(
                listOf(
                    Seat(SeatRow("B"), SeatColumn(2), SeatGrade.B),
                ),
            )

        val exception =
            assertThrows<IllegalArgumentException> {
                screening.isReserveAvailable(selectedSeats)
            }
        assertThat(exception.message).isEqualTo("이미 예약된 좌석입니다.")
    }

    @Test
    fun `여러 영화를 예매하고 한꺼번에 결제할 수 있다`() {
        val movies = movieRepository.findAll()
        val date = LocalDate.of(2025, 9, 20)

        val f1Movie = movies.findMovie("F1 더 무비")
        val f1Screening = f1Movie.getScreeningsByDate(date).findByNumber(1)
        val f1Seats =
            SelectedSeats(
                listOf(Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S)),
            )
        f1Screening.isReserveAvailable(f1Seats)
        val f1Reserved = f1Screening.reserve(f1Seats)
        val f1Reservation = f1Reserved.createReservation(f1Seats)

        val toyMovie = movies.findMovie("토이 스토리")
        val toyScreening = toyMovie.getScreeningsByDate(date).findByNumber(1)
        val toySeats =
            SelectedSeats(
                listOf(Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B)),
            )
        toyScreening.isReserveAvailable(toySeats)
        val toyReserved = toyScreening.reserve(toySeats)
        val toyReservation = toyReserved.createReservation(toySeats)

        val reservations = Reservations(listOf(f1Reservation, toyReservation))

        val priceCalculator =
            PriceCalculator(
                DiscountPolicies(
                    listOf(MovieDayDiscount()),
                    listOf(TimeDiscount()),
                ),
            )
        val paymentResult =
            priceCalculator.calculate(
                reservations,
                Point(0),
                Cash(),
            )

        val reservationId =
            reservationRepository.save(
                reservations,
                paymentResult.totalPrice,
                paymentResult.usedPoint,
                paymentResult.paymentMethodName(),
            )
        assertThat(reservationId).isGreaterThan(0L)

        val updatedMovies = movieRepository.findAll()

        val updatedF1 =
            updatedMovies
                .findMovie("F1 더 무비")
                .getScreeningsByDate(date)
                .findByNumber(1)
        assertThat(
            updatedF1.isSeatAvailable(
                Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S),
            ),
        ).isFalse()

        val updatedToy =
            updatedMovies
                .findMovie("토이 스토리")
                .getScreeningsByDate(date)
                .findByNumber(1)
        assertThat(
            updatedToy.isSeatAvailable(
                Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B),
            ),
        ).isFalse()
    }

    @Test
    fun `할인이 올바르게 적용된다`() {
        val movies = movieRepository.findAll()
        val movie = movies.findMovie("F1 더 무비")
        val screening = movie.getScreeningsByDate(LocalDate.of(2025, 9, 20)).findByNumber(1)

        val selectedSeats =
            SelectedSeats(
                listOf(Seat(SeatRow("C"), SeatColumn(1), SeatGrade.S)), // 18000원
            )
        screening.isReserveAvailable(selectedSeats)
        val reserved = screening.reserve(selectedSeats)
        val reservation = reserved.createReservation(selectedSeats)
        val reservations = Reservations(listOf(reservation))

        val priceCalculator =
            PriceCalculator(
                DiscountPolicies(
                    listOf(MovieDayDiscount()),
                    listOf(TimeDiscount()),
                ),
            )

        val result =
            priceCalculator.calculate(
                reservations,
                Point(2000),
                CreditCard(),
            )

        assertThat(result.totalPrice).isEqualTo(Money(11590))
        assertThat(result.usedPoint).isEqualTo(Point(2000))
    }

    @Test
    fun `시간이 겹치는 상영은 동시에 예매할 수 없다`() {
        val movies = movieRepository.findAll()
        val date = LocalDate.of(2025, 9, 20)

        val f1Movie = movies.findMovie("F1 더 무비")
        val f1Screening = f1Movie.getScreeningsByDate(date).findByNumber(1)
        val f1Seats =
            SelectedSeats(
                listOf(Seat(SeatRow("A"), SeatColumn(1), SeatGrade.B)),
            )
        val f1Reservation = f1Screening.reserve(f1Seats).createReservation(f1Seats)

        val ironMan = movies.findMovie("아이언맨")
        val ironScreening = ironMan.getScreeningsByDate(date).findByNumber(1)
        val ironSeats =
            SelectedSeats(
                listOf(Seat(SeatRow("A"), SeatColumn(2), SeatGrade.B)),
            )
        val ironReservation = ironScreening.reserve(ironSeats).createReservation(ironSeats)

        val exception =
            assertThrows<IllegalArgumentException> {
                Reservations(listOf(f1Reservation, ironReservation))
            }
        assertThat(exception.message).isEqualTo("상영 시간이 겹치는 예매가 존재합니다.")
    }
}
