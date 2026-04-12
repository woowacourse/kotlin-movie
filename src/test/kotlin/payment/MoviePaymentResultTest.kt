package payment

import model.movie.Movie
import model.movie.MovieName
import model.movie.RunningTime
import model.payment.DefaultMoviePayment
import model.payment.EarlyMorningDiscount
import model.payment.LateNightDiscount
import model.payment.Money
import model.payment.MovieDayDiscount
import model.payment.MoviePaymentResult
import model.payment.PayType
import model.payment.PayTypeDiscount
import model.payment.Point
import model.payment.PointDiscount
import model.payment.SequentialMovieDiscount
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDateTime

class MoviePaymentResultTest {
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

        assertThat(
            DefaultMoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(2)),
                                        grade = SeatGrade.S,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
                sequentialMovieDiscount = SequentialMovieDiscount(emptyList()),
                pointDiscount = PointDiscount(Point(0)),
                payTypeDiscount = null,
            ).calculate(),
        ).isEqualTo(MoviePaymentResult(Money(18_000), Money(18_000)))
    }

    @Test
    fun `무비데이(매월 10일, 20일, 30일)에 상영되는 영화는 10% 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 0)),
            )

        val defaultMoviePayment =
            DefaultMoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
                sequentialMovieDiscount = SequentialMovieDiscount(listOf(MovieDayDiscount())),
                pointDiscount = PointDiscount(Point(0)),
                payTypeDiscount = null,
            )
        assertThat(defaultMoviePayment.calculate()).isEqualTo(
            MoviePaymentResult(
                Money(15_000),
                Money(13_500),
            ),
        )
    }

    @Test
    fun `오전 11시까지 시작하는 상영은 2,000원이 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 10, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 11, 0)),
            )

        val defaultMoviePayment =
            DefaultMoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
                sequentialMovieDiscount = SequentialMovieDiscount(listOf(EarlyMorningDiscount())),
                pointDiscount = PointDiscount(Point(0)),
                payTypeDiscount = null,
            )
        assertThat(
            defaultMoviePayment.calculate(),
        ).isEqualTo(MoviePaymentResult(Money(15_000), Money(13_000)))
    }

    @Test
    fun `오후 8시부터 시작하는 상영은 2,000원이 할인된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val defaultMoviePayment =
            DefaultMoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
                sequentialMovieDiscount = SequentialMovieDiscount(listOf(LateNightDiscount())),
                pointDiscount = PointDiscount(Point(0)),
                payTypeDiscount = null,
            )
        assertThat(
            defaultMoviePayment.calculate(),
        ).isEqualTo(MoviePaymentResult(Money(15_000), Money(13_000)))
    }

    @Test
    fun `포인트를 사용하면 예매 금액에서 차감된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val defaultMoviePayment =
            DefaultMoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
                sequentialMovieDiscount = SequentialMovieDiscount(emptyList()),
                pointDiscount = PointDiscount(Point(5000)),
                payTypeDiscount = null,
            )
        assertThat(
            defaultMoviePayment.calculate(),
        ).isEqualTo(MoviePaymentResult(Money(15_000), Money(10_000)))
    }

    @ParameterizedTest
    @CsvSource(
        "CREDIT_CARD, 14_250",
        "CASH, 14_700",
    )
    fun `결제 방식에 따라 결제 금액에서 일정 비율 할인된다`(
        payType: PayType,
        finalPrice: Int,
    ) {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 21, 0)),
            )

        val defaultMoviePayment =
            DefaultMoviePayment(
                reservations =
                    MovieReservationGroup(
                        setOf(
                            MovieReservationResult(
                                movie = movieOne,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        position = SeatPosition(SeatRow("A"), SeatColumn(1)),
                                        grade = SeatGrade.A,
                                    ),
                                state = SeatState.RESERVED,
                            ),
                        ),
                    ),
                sequentialMovieDiscount = SequentialMovieDiscount(emptyList()),
                pointDiscount = PointDiscount(Point(0)),
                payTypeDiscount = PayTypeDiscount(payType),
            )
        assertThat(
            defaultMoviePayment.calculate(),
        ).isEqualTo(
            MoviePaymentResult(
                Money(15_000),
                Money(finalPrice),
            ),
        )
    }
}
