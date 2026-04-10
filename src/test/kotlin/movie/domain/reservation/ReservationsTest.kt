package movie.domain.reservation

import movie.domain.amount.Price
import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
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
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class ReservationsTest {
    @Test
    fun `예매를 추가할 수 있다`() {
        // given
        val reservationData = ReservationData.reservations
        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("C", 1, SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(13, 0),
                    LocalTime.of(14, 0),
                ),
                ReservedSeats(
                    setOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )
        val movie = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening)))
        val addedReservation = Reservation(movie, screening, selectedSeats)
        val reservations = Reservations(reservationData)

        // when
        val result = reservations.add(addedReservation)

        // then
        assertThat(result).isEqualTo(reservationData + addedReservation)
    }

    @Test
    fun `시간이 겹치는 예매를 추가할 수 없다`() {
        val reservationData = ReservationData.reservations
        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("C", 1, SeatGrade.S),
                ),
            )

        val screening =
            Screening(
                Screen(1, Seats.createDefault()),
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(10, 0),
                    LocalTime.of(13, 0),
                ),
                ReservedSeats(
                    setOf(
                        Seat("C", 1, SeatGrade.S),
                        Seat("C", 2, SeatGrade.S),
                        Seat("E", 1, SeatGrade.A),
                    ),
                ),
            )
        val movie = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening)))
        val addedReservation = Reservation(movie, screening, selectedSeats)
        val reservations = Reservations(reservationData)

        val exception =
            assertThrows<IllegalArgumentException> {
                reservations.add(addedReservation)
            }
        assert(exception.message == "상영 시간이 겹치는 예매가 존재합니다.")
    }

    @Test
    fun `무비데이에 상영하는 영화의 A등급 좌석 2개, 시간 할인이 적용되는 영화의 A등급 좌석 2개를 구매했을 때의 가격은 55000원이다`() {
        // given
        val screen = Screen(1, Seats.createDefault())

        val selectedSeats =
            SelectedSeats(
                setOf(
                    Seat("E", 1, SeatGrade.A),
                    Seat("E", 2, SeatGrade.A),
                ),
            )

        val screening1 =
            Screening(
                screen,
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 10),
                    LocalTime.of(12, 0),
                    LocalTime.of(14, 0),
                ),
                ReservedSeats(emptySet()),
            )

        val screening2 =
            Screening(
                screen,
                ScreeningDateTime(
                    LocalDate.of(2026, 1, 1),
                    LocalTime.of(9, 0),
                    LocalTime.of(12, 0),
                ),
                ReservedSeats(emptySet()),
            )

        val movie1 = Movie(title = "F1 더 무비", screenings = Screenings(listOf(screening1)))
        val movie2 = Movie(title = "토이 스토리", screenings = Screenings(listOf(screening2)))

        val reservations =
            Reservations(
                listOf(
                    Reservation(movie1, screening1, selectedSeats),
                    Reservation(movie2, screening2, selectedSeats),
                ),
            )

        // then
        assert(
            reservations.discountedTotalPrice(
                DiscountPolicies(
                    listOf(MovieDayDiscount()),
                    listOf(TimeDiscount()),
                ),
            ) == Price(55000),
        )
    }
}
