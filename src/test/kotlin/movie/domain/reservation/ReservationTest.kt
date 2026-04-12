package movie.domain.reservation

import movie.data.SeatsData
import movie.domain.amount.Price
import movie.domain.movie.Movie
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservationTest {
    @Test
    fun `선택한 좌석들의 총 가격을 계산할 수 있다`() {
        // given
        val selectedSeats =
            SelectedSeats(
                Seats(
                    setOf(
                        Seat("A", 1, SeatGrade.B),
                        Seat("C", 1, SeatGrade.S),
                    ),
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
        val movie = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening)))
        val reservation = Reservation(movie, screening, selectedSeats)

        // when
        val price = reservation.calculatePrice()

        // then
        assertThat(price).isEqualTo(Price(30000))
    }
}
