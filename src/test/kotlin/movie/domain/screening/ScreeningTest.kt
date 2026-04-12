package movie.domain.screening

import movie.data.SeatsData
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ScreeningTest {
    @Test
    fun `존재하지 하지 않는 좌석은 예약할 수 없다`() {
        val selectedSeats =
            Seats(
                setOf(
                    Seat("A", 1, SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                Screen(1, SeatsData.seats),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservedSeats(
                    Seats(
                        setOf(
                            Seat("C", 1, SeatGrade.S),
                            Seat("C", 2, SeatGrade.S),
                            Seat("E", 1, SeatGrade.A),
                        ),
                    ),
                ),
            )

        val exception =
            assertThrows<IllegalArgumentException> {
                screening.reserve(selectedSeats)
            }
        assert(exception.message == "존재하지 않는 좌석입니다.")
    }

    @Test
    fun `이미 예약된 좌석들은 예약이 불가능하다`() {
        val selectedSeats =
            Seats(
                setOf(
                    Seat("C", 1, SeatGrade.S),
                    Seat("C", 2, SeatGrade.S),
                    Seat("E", 1, SeatGrade.A),
                ),
            )

        val screening =
            Screening(
                Screen(1, SeatsData.seats),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservedSeats(
                    Seats(
                        setOf(
                            Seat("C", 1, SeatGrade.S),
                            Seat("C", 2, SeatGrade.S),
                            Seat("E", 1, SeatGrade.A),
                        ),
                    ),
                ),
            )
        val exception =
            assertThrows<IllegalArgumentException> {
                screening.reserve(selectedSeats)
            }
        assert(exception.message == "이미 예약된 좌석입니다.")
    }

    @Test
    fun `존재하는 좌석이며 예약이 되지 않은 좌석은 예약이 가능하다`() {
        val selectedSeats =
            Seats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                ),
            )

        val screening =
            Screening(
                Screen(1, SeatsData.seats),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0),
                ),
                ReservedSeats(
                    Seats(
                        setOf(
                            Seat("C", 1, SeatGrade.S),
                            Seat("C", 2, SeatGrade.S),
                            Seat("E", 1, SeatGrade.A),
                        ),
                    ),
                ),
            )

        val result = screening.reserve(selectedSeats)
        assertThat(result.reservedSeats.getSeats()).containsAll(selectedSeats.seats + screening.reservedSeats.getSeats())
    }
}
