@file:Suppress("NonAsciiCharacters")

package model.policy

import model.Money
import model.Screen
import model.movie.Movie
import model.movie.RunningTime
import model.reservation.Reservation
import model.reservation.Reservations
import model.screening.Screening
import model.screening.ScreeningSeatMap
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountSystemTest {
    private val movie = Movie("영화", RunningTime(120))
    private val seats = Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.S))) // 18,000원
    private val screen = Screen("1관", seats)

    @Test
    fun `할인 정책이 없을 경우 할인되지 않은 원가 그대로를 반환한다`() {
        // given: 할인 정책이 빈 리스트인 할인 시스템 생성
        val discountSystem = DiscountSystem(emptyList())
        val screening = Screening(movie, LocalDateTime.of(2024, 1, 1, 12, 0), ScreeningSeatMap(screen))
        val reservations = Reservations(listOf(Reservation(screening, seats)))

        // when: 예매 내역의 할인된 금액을 계산
        val result = discountSystem.discountPrice(reservations)

        // then: 할인되지 않은 금액인 18,000원이 반환
        assertThat(result).isEqualTo(Money(18_000))
    }

    @Test
    fun `정액 할인 정책(TimeDiscountPolicy)이 적용될 경우 해당 금액만큼 할인된다`() {
        // given: TimeDiscountPolicy(2,000원 할인)가 적용되는 시간(오전 10시)의 상영 정보 생성
        val discountSystem = DiscountSystem(listOf(TimeDiscountPolicy))
        val morningScreening = Screening(movie, LocalDateTime.of(2024, 1, 1, 10, 0), ScreeningSeatMap(screen))
        val reservations = Reservations(listOf(Reservation(morningScreening, seats)))

        // when: 18,000원 예매 내역에 대해 할인된 금액을 계산
        val result = discountSystem.discountPrice(reservations)

        // then: 2,000원이 할인된 16,000원이 반환된다
        assertThat(result).isEqualTo(Money(16_000))
    }

    @Test
    @DisplayName("정률 할인 정책(MovieDayDiscountPolicy)이 적용될 경우 해당 비율만큼 할인된다")
    fun `정률 할인 정책(MovieDayDiscountPolicy)이 적용될 경우 해당 비율만큼 할인된다`() {
        // given: MovieDayDiscountPolicy(10% 할인)가 적용되는 날짜(10일)의 상영 정보 생성
        val discountSystem = DiscountSystem(listOf(MovieDayDiscountPolicy))
        val discountDayScreening = Screening(movie, LocalDateTime.of(2024, 1, 10, 12, 0), ScreeningSeatMap(screen))
        val reservations = Reservations(listOf(Reservation(discountDayScreening, seats)))

        // when: 18,000원 예매 내역에 대해 할인된 금액을 계산
        val result = discountSystem.discountPrice(reservations)

        // then: 1,800원이 할인된 16,200원이 반환
        assertThat(result).isEqualTo(Money(16_200))
    }

    @Test
    fun `여러 개의 예약이 있을 경우 각각의 할인이 모두 적용된 합계 금액을 반환한다`() {
        // given: TimeDiscountPolicy가 적용되는 2개의 예약(각 18,000원) 생성
        val discountSystem = DiscountSystem(listOf(TimeDiscountPolicy))
        val morningScreening = Screening(movie, LocalDateTime.of(2024, 1, 1, 10, 0), ScreeningSeatMap(screen))
        val reservation1 = Reservation(morningScreening, seats)
        val reservation2 = Reservation(morningScreening, seats)
        val multipleReservations = Reservations(listOf(reservation1, reservation2))

        // when: 전체 할인된 금액을 계산
        val result = discountSystem.discountPrice(multipleReservations)

        // then: 각각 2,000원씩 할인되어 (16,000 * 2) = 32,000원이 반환
        assertThat(result).isEqualTo(Money(32_000))
    }

    @Test
    fun `정책 순서를 Time, MovieDay 순으로 생성해도 MovieDay, Time 순으로 적용된다`() {
        // given: 날짜 할인(10%)과 시간 할인(2,000원)이 모두 적용되는 상영 정보 생성
        // 10일(날짜 할인) 오전 10시(시간 할인)
        val screening = Screening(movie, LocalDateTime.of(2024, 1, 10, 10, 0), ScreeningSeatMap(screen))
        val reservations = Reservations(listOf(Reservation(screening, seats)))

        // when: (시간 할인, 날짜 할인) 순서로 정책이 적용되어도
        val discountSystem2 = DiscountSystem(listOf(TimeDiscountPolicy, MovieDayDiscountPolicy))
        val result2 = discountSystem2.discountPrice(reservations)

        // then: 무비데이 10프로 할인 적용 후 시간할인 2000원 적용
        // 18,000 - 1800 - 2000 = 14,200
        assertThat(result2).isEqualTo(Money(14_200))
    }
}
