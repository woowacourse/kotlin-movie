package domain

import domain.fixture.createMovie
import domain.fixture.createScreening
import domain.fixture.createSeatPositions
import domain.payment.Money
import domain.payment.PaymentSystem
import domain.payment.PaymentType
import domain.payment.Point
import domain.reservation.Ticket
import domain.reservation.TicketBucket
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.stream.Stream

class PaymentSystemTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideDiscountCases")
    fun `총 결제 금액이 올바르게 계산된다`(
        description: String,
        givenPoint: Point,
        givenPayment: PaymentType,
        givenStartTime: LocalDateTime,
        expected: Money,
    ) {
        val paymentSystem = PaymentSystem()
        val result =
            paymentSystem.calculate(
                point = givenPoint,
                payment = givenPayment,
                ticketBucket =
                    TicketBucket(
                        tickets =
                            listOf(
                                Ticket(
                                    screening = createScreening(startTime = givenStartTime),
                                    seatPositions =
                                        createSeatPositions(
                                            positions =
                                                arrayOf(
                                                    "A" to 1,
                                                ),
                                        ),
                                ),
                            ),
                    ),
            )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `여러 좌석을 예매한 경우 올바르게 계산한다`() {
        val paymentSystem = PaymentSystem()
        val result =
            paymentSystem.calculate(
                point = Point(6000),
                payment = PaymentType.CREDIT_CARD,
                ticketBucket =
                    TicketBucket(
                        tickets =
                            listOf(
                                Ticket(
                                    screening = createScreening(),
                                    seatPositions =
                                        createSeatPositions(
                                            positions =
                                                arrayOf(
                                                    "A" to 1,
                                                    "C" to 1,
                                                    "E" to 1,
                                                    "E" to 2,
                                                ),
                                        ),
                                ),
                            ),
                    ),
            )
        assertThat(result).isEqualTo(Money(38000))
    }

    @Test
    fun `여러 영화를 예매한 경우 올바르게 계산한다`() {
        val paymentSystem = PaymentSystem()
        val result =
            paymentSystem.calculate(
                point = Point(600),
                payment = PaymentType.CREDIT_CARD,
                ticketBucket =
                    TicketBucket(
                        tickets =
                            listOf(
                                Ticket(
                                    screening = createScreening(),
                                    seatPositions =
                                        createSeatPositions(
                                            positions =
                                                arrayOf(
                                                    "A" to 1,
                                                ),
                                        ),
                                ),
                                Ticket(
                                    screening = createScreening(movie = createMovie(title = "영화 2")),
                                    seatPositions =
                                        createSeatPositions(
                                            positions =
                                                arrayOf(
                                                    "A" to 1,
                                                ),
                                        ),
                                ),
                            ),
                    ),
            )
        assertThat(result).isEqualTo(Money(16150))
    }

    @Test
    fun `할인 적용 후 금액이 0보다 작을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            PaymentSystem().calculate(
                point = Point(10001),
                payment = PaymentType.CREDIT_CARD,
                ticketBucket =
                    TicketBucket(
                        listOf(
                            Ticket(
                                screening =
                                    createScreening(
                                        startTime =
                                            LocalDateTime.of(
                                                2026,
                                                4,
                                                8,
                                                10,
                                                0,
                                            ),
                                    ),
                                seatPositions = createSeatPositions("A" to 1),
                            ),
                        ),
                    ),
            )
        }
    }

    companion object {
        private val MOVIE_DAY = LocalDate.of(2026, 4, 10)
        private val NO_MOVIE_DAY = LocalDate.of(2026, 4, 15)
        private val DISCOUNT_TIME = LocalTime.of(10, 0) // 11시 이전
        private val NO_DISCOUNT_TIME = LocalTime.of(15, 0) // 11~20시

        @JvmStatic
        fun provideDiscountCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    "무비데이+시간+포인트+카드",
                    Point(800),
                    PaymentType.CREDIT_CARD,
                    LocalDateTime.of(MOVIE_DAY, DISCOUNT_TIME),
                    Money(7600),
                ),
                Arguments.of(
                    "무비데이+시간+포인트+현금",
                    Point(800),
                    PaymentType.CASH,
                    LocalDateTime.of(MOVIE_DAY, DISCOUNT_TIME),
                    Money(7840),
                ),
                Arguments.of(
                    "시간+카드",
                    Point(0),
                    PaymentType.CREDIT_CARD,
                    LocalDateTime.of(NO_MOVIE_DAY, DISCOUNT_TIME),
                    Money(9500),
                ),
                Arguments.of(
                    "무비데이+카드",
                    Point(0),
                    PaymentType.CREDIT_CARD,
                    LocalDateTime.of(MOVIE_DAY, NO_DISCOUNT_TIME),
                    Money(10260),
                ),
            )
    }
}
