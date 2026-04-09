package domain.screening

import domain.seat.ReservatedSeats
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ScreeningTest {
    @Test
    fun `존재하지 하지 않는 좌석은 예약할 수 없다`() {
        val selectedSeats =
            listOf(
                Seat("A", 1, SeatGrade.S),
            )

        val screening =
            Screening(
                "토이 스토리",
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservatedSeats(
                    listOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )

        val exception =
            assertThrows<IllegalArgumentException> {
                screening.isReserveAvailable(selectedSeats)
            }
        assert(exception.message == "존재하지 않는 좌석입니다.")
    }

    @Test
    fun `예약되지 않은 좌석은 예약할 수 있다`() {
        val selectedSeats =
            listOf(
                Seat("A", 1, SeatGrade.B),
            )

        val screening =
            Screening(
                "토이 스토리",
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservatedSeats(
                    listOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )

        val result = screening.isReserveAvailable(selectedSeats)

        assertThat(result).isEqualTo(selectedSeats)
    }

    @Test
    fun `이미 예약된 좌석들은 예약이 불가능하다`() {
        val selectedSeats =
            listOf(
                Seat("C", 1, SeatGrade.S),
                Seat("C", 2, SeatGrade.S),
                Seat("E", 1, SeatGrade.A),
            )

        val screening =
            Screening(
                "토이 스토리",
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservatedSeats(
                    listOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )
        val exception =
            assertThrows<IllegalArgumentException> {
                screening.isReserveAvailable(selectedSeats)
            }
        assert(exception.message == "이미 예약된 좌석입니다.")
    }

    @Test
    fun `예약 검증 후 true일 경우 예약이 생성된다`() {
        val selectedSeats =
            listOf(
                Seat("A", 1, SeatGrade.S),
            )

        val screening =
            Screening(
                "토이 스토리",
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservatedSeats(
                    listOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )

        val result = screening.reserve(selectedSeats)
        assertThat(result).isNotNull()
    }
}
