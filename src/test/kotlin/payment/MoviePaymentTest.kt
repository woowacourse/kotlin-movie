package payment

import model.movie.Movie
import model.movie.MovieName
import model.movie.RunningTime
import model.payment.Money
import model.payment.MoviePayment
import model.payment.PayType
import model.payment.Point
import model.reservation.MovieReservationGroup
import model.reservation.MovieReservationResult
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatPosition
import model.seat.SeatRow
import model.seat.SeatState
import model.time.CinemaTime
import model.time.CinemaTimeRange
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MoviePaymentTest {
    private val movieOne =
        Movie(
            name = MovieName("혼자사는남자"),
            runningTime = RunningTime(60),
        )

    @Test
    fun `좌석 등급과 예약한 좌석 수에 따라서 가격이 계산된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 11, 21, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 11, 22, 0)),
            )

        Assertions
            .assertThat(
                MoviePayment(
                    reservations =
                        MovieReservationGroup(
                            setOf(
                                MovieReservationResult(
                                    movie = movieOne,
                                    startTime = screenTime.start,
                                    seat =
                                        Seat(
                                            position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                            grade = SeatGrade.A,
                                        ),
                                    state = SeatState.RESERVED,
                                ),
                                MovieReservationResult(
                                    movie = movieOne,
                                    startTime = screenTime.start,
                                    seat =
                                        Seat(
                                            position = SeatPosition(SeatRow("A"), SeatColumn(2)),
                                            grade = SeatGrade.S,
                                        ),
                                    state = SeatState.RESERVED,
                                ),
                            ),
                        ),
                ).totalPrice,
            ).isEqualTo(Money(33_000))
    }

    @Test
    fun `무비데이(매월 10일, 20일, 30일)에 상영되는 영화는 10% 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(2)),
                                        grade = SeatGrade.S,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.discountMovieDay()
        Assertions
            .assertThat(
                moviePayment.currentPrice,
            ).isEqualTo(Money(29_700))
    }

    @Test
    fun `오전 11시까지 시작하는 상영은 2,000원이 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 10, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 11, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.discountTime()
        Assertions
            .assertThat(
                moviePayment.currentPrice,
            ).isEqualTo(Money(15_000) - Money(2_000))
    }

    @Test
    fun `오후 8시부터 시작하는 상영은 2,000원이 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.discountTime()
        Assertions
            .assertThat(
                moviePayment.currentPrice,
            ).isEqualTo(Money(15_000) - Money(2_000))
    }

    @Test
    fun `무비데이이면서 시간 조건을 만족하면 무비데이 할인이 먼저 적용된 후 시간 조건 할인이 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.discount()
        Assertions
            .assertThat(
                moviePayment.currentPrice,
            ).isEqualTo(Money(11_500))
    }

    @Test
    fun `포인트를 사용하면 예매 금액에서 차감된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.applyPoint(Point(5000))
        Assertions
            .assertThat(
                moviePayment.currentPrice,
            ).isEqualTo(Money(10_000))
    }

    @Test
    fun `신용카드로 결제하면 포인트 적용 후 금액에서 5% 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.applyPoint(Point(5000))
        Assertions
            .assertThat(
                moviePayment.pay(PayType.CREDIT_CARD),
            ).isEqualTo(Money(9_500))
    }

    @Test
    fun `현금으로 결제하면 포인트 적용 후 금액에서 2% 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.applyPoint(Point(5000))
        Assertions
            .assertThat(
                moviePayment.pay(PayType.CASH),
            ).isEqualTo(Money(9_800))
    }

    @Test
    fun `할인 조건이 없으면 기본 금액이 그대로 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 13, 13, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 13, 14, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                startTime = screenTime.start,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
            )
        moviePayment.discount()
        moviePayment.applyPoint(Point(0))
        Assertions
            .assertThat(
                moviePayment.totalPrice,
            ).isEqualTo(moviePayment.currentPrice)
    }
}
