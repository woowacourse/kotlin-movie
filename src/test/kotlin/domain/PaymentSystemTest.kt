package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/*
- [ ] 할인 적용 후 금액이 0보다 작을 경우 예외를 던진다
- [ ] 총 결제 금액이 올바르게 계산된다
 */
class PaymentSystemTest {

    @Test
    fun `총 결제 금액이 올바르게 계산된다`() {
        val paymentSystem = PaymentSystem(discountPolicy = DiscountPolicy())
        val result = paymentSystem.calculate(
            point = Point(800),
            payment = PaymentType.CREDIT_CARD,
            ticketBucket = TicketBucket(
                tickets =
                    listOf(
                        Ticket(
                            screening =
                                Screening(
                                    movie =
                                        Movie(
                                            title = Title("허닛"),
                                            runningTime = RunningTime(167),
                                            screeningPeriod =
                                                ScreeningPeriod(
                                                    startDate = LocalDate.of(2026, 4, 8),
                                                    endDate = LocalDate.of(2026, 4, 9),
                                                ),
                                        ),
                                    room =
                                        ScreeningRoom(
                                            name = ScreeningRoomName("커피"),
                                            operatingTime = TimeRange(
                                                LocalTime.of(10, 0),
                                                LocalTime.of(18, 0)
                                            ),
                                            seats =
                                                Seats(
                                                    listOf(
                                                        Seat(
                                                            position =
                                                                SeatPosition(
                                                                    Row("A"),
                                                                    Column(1),
                                                                ),
                                                        ),
                                                    ),
                                                ),
                                        ),
                                    startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
                                ),
                            seatPositions = SeatPositions(
                                listOf(
                                    SeatPosition(
                                        Row("A"),
                                        Column(1)
                                    )
                                )
                            ),
                        ),
                    ),
            )
        )
        assertThat(result).isEqualTo(Money(7600))
    }

    @Test
    fun `할인 적용 후 금액이 0보다 작을 경우 예외를 던진다`() {
        val paymentSystem = PaymentSystem(discountPolicy = DiscountPolicy())
        assertThrows(IllegalArgumentException::class.java) {
            paymentSystem.calculate(
                point = Point(10001),
                payment = PaymentType.CREDIT_CARD,
                ticketBucket = TicketBucket(
                    tickets =
                        listOf(
                            Ticket(
                                screening =
                                    Screening(
                                        movie =
                                            Movie(
                                                title = Title("허닛"),
                                                runningTime = RunningTime(167),
                                                screeningPeriod =
                                                    ScreeningPeriod(
                                                        startDate = LocalDate.of(2026, 4, 8),
                                                        endDate = LocalDate.of(2026, 4, 9),
                                                    ),
                                            ),
                                        room =
                                            ScreeningRoom(
                                                name = ScreeningRoomName("커피"),
                                                operatingTime = TimeRange(
                                                    LocalTime.of(10, 0),
                                                    LocalTime.of(18, 0)
                                                ),
                                                seats =
                                                    Seats(
                                                        listOf(
                                                            Seat(
                                                                position =
                                                                    SeatPosition(
                                                                        Row("A"),
                                                                        Column(1),
                                                                    ),
                                                            ),
                                                        ),
                                                    ),
                                            ),
                                        startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                                    ),
                                seatPositions = SeatPositions(
                                    listOf(
                                        SeatPosition(
                                            Row("A"),
                                            Column(1)
                                        )
                                    )
                                ),
                            ),
                        ),
                )
            )
        }
    }
}
