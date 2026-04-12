package model.payment

import model.CinemaTime
import model.CinemaTimeRange
import model.MovieReservationResult
import model.fixture.MovieFixture
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatRow
import model.seat.SeatState
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MoviePaymentTest {
    private val movie = MovieFixture.create()

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
                        listOf(
                            MovieReservationResult.Success(
                                movie = movie,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        row = SeatRow("A"),
                                        column = SeatColumn(1),
                                        grade = SeatGrade.A,
                                        state = SeatState.RESERVED,
                                    ),
                            ),
                            MovieReservationResult.Success(
                                movie = movie,
                                screenTime = screenTime,
                                seat =
                                    Seat(
                                        row = SeatRow("A"),
                                        column = SeatColumn(2),
                                        grade = SeatGrade.S,
                                        state = SeatState.RESERVED,
                                    ),
                            ),
                        ),
                ).originalPrice,
            ).isEqualTo(33_000)
    }

    @Test
    fun `무비데이(매월 10일, 20일, 30일)에 상영되는 영화를 신용카드로 결제 시 무비데이 10% 할인과 신용카드 결제 5% 할인이 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 13, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 14, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    listOf(
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(2),
                                    grade = SeatGrade.S,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                    ),
            )

        Assertions
            .assertThat(
                moviePayment.getFinalPrice(payType = PayType.CREDIT_CARD, point = 0),
            ).isEqualTo(28_215)
    }

    @Test
    fun `무비데이(매월 10일, 20일, 30일)에 상영되는 영화를 현금으로 결제 시 무비데이 10% 할인과 현금 결제 2% 할인이 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 13, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 14, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    listOf(
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(2),
                                    grade = SeatGrade.S,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                    ),
            )

        Assertions
            .assertThat(
                moviePayment.getFinalPrice(payType = PayType.CASH, point = 0),
            ).isEqualTo(29_106)
    }

    @Test
    fun `무비데이(매월 10일, 20일, 30일)에 상영되는 영화를 현금과 포인트를 사용하여 결제 시 무비데이 10% 할인과 현금 결제 2% 할인, 포인트가 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 13, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 14, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    listOf(
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(2),
                                    grade = SeatGrade.S,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                    ),
            )

        Assertions
            .assertThat(
                moviePayment.getFinalPrice(payType = PayType.CASH, point = 3000),
            ).isEqualTo(26_166)
    }

    @Test
    fun `무비데이가 아닌 날 오전 11시 전에 상영되는 영화를 신용카드로 결제하면 2000원 할인 + 5% 할인이 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 15, 10, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 15, 11, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    listOf(
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                    ),
            )
        Assertions
            .assertThat(
                moviePayment.getFinalPrice(payType = PayType.CREDIT_CARD, point = 0),
            ).isEqualTo(12_350)
    }

    @Test
    fun `무비데이가 아닌 날 오후 8시 후에 상영되는 영화를 신용카드로 결제하면 2000원 할인 + 5% 할인이 적용된다`() {
        val screenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 15, 20, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 15, 21, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    listOf(
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = screenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                    ),
            )
        Assertions
            .assertThat(
                moviePayment.getFinalPrice(payType = PayType.CREDIT_CARD, point = 0),
            ).isEqualTo(12_350)
    }

    @Test
    fun `무비데이인 날과 무비데이가 아닌 날의 영화를 동시 예매했을 때 무비데이 예약건만 할인이 진행된다`() {
        val movieDayScreenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 10, 15, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 10, 16, 0)),
            )

        val noMovieDayScreenTime =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 15, 15, 0)),
                CinemaTime(LocalDateTime.of(2026, 4, 15, 16, 0)),
            )

        val moviePayment =
            MoviePayment(
                reservations =
                    listOf(
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = movieDayScreenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                        MovieReservationResult.Success(
                            movie = movie,
                            screenTime = noMovieDayScreenTime,
                            seat =
                                Seat(
                                    row = SeatRow("A"),
                                    column = SeatColumn(1),
                                    grade = SeatGrade.A,
                                    state = SeatState.RESERVED,
                                ),
                        ),
                    ),
            )
        Assertions
            .assertThat(
                moviePayment.getFinalPrice(payType = PayType.CREDIT_CARD, point = 0),
            ).isEqualTo(27_075)
    }
}
