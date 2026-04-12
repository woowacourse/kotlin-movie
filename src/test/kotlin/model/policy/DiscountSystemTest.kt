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
    private val movie = Movie("테스트 영화", RunningTime(120))
    private val seats = Seats(listOf(Seat(SeatNumber('C', 1), SeatGrade.S))) // 18,000원
    private val screen = Screen("1관", seats)
    private val screening = Screening(movie, LocalDateTime.of(2026, 4, 13, 12, 0), ScreeningSeatMap(screen))
    private val reservations = Reservations(listOf(Reservation(screening, seats)))

    class FakeAmountDiscountPolicy(private val amount: Int, override val priority: Int = 2) : DiscountPolicy {
        override fun findDiscount(screening: Screening): Discount = AmountDiscount(Money(amount))
    }

    class FakePercentDiscountPolicy(private val percent: Int, override val priority: Int = 1) : DiscountPolicy {
        override fun findDiscount(screening: Screening): Discount = PercentDiscount(percent)
    }

    @Test
    fun `할인 정책이 없을 경우 할인되지 않은 원가 그대로를 반환한다`() {
        // given: 할인 정책이 빈 리스트인 할인 시스템 생성
        val discountSystem = DiscountSystem(emptyList())

        // when: 예매 내역의 할인된 금액을 계산
        val result = discountSystem.discountPrice(reservations)

        // then: 할인되지 않은 금액인 18,000원이 반환
        assertThat(result).isEqualTo(Money(18_000))
    }

    @Test
    fun `정액 할인 정책이 적용될 경우 해당 금액만큼 할인된다`() {
        // given: 항상 2,000원을 할인해 주는 가짜 정책 주입
        val fakeAmountPolicy = FakeAmountDiscountPolicy(2_000)
        val discountSystem = DiscountSystem(listOf(fakeAmountPolicy))

        // when: 18,000원 예매 내역에 대해 할인된 금액을 계산
        val result = discountSystem.discountPrice(reservations)

        // then: 2,000원이 할인된 16,000원이 반환된다
        assertThat(result).isEqualTo(Money(16_000))
    }

    @Test
    @DisplayName("정률 할인 정책이 적용될 경우 해당 비율만큼 할인된다")
    fun `정률 할인 정책이 적용될 경우 해당 비율만큼 할인된다`() {
        // given: 항상 10%를 할인해 주는 가짜 정책 주입
        val fakePercentPolicy = FakePercentDiscountPolicy(10)
        val discountSystem = DiscountSystem(listOf(fakePercentPolicy))

        // when: 18,000원 예매 내역에 대해 할인된 금액을 계산
        val result = discountSystem.discountPrice(reservations)

        // then: 1,800원이 할인된 16,200원이 반환
        assertThat(result).isEqualTo(Money(16_200))
    }

    @Test
    fun `여러 개의 예약이 있을 경우 각각의 할인이 모두 적용된 합계 금액을 반환한다`() {
        // given: 항상 2,000원을 할인해 주는 가짜 정책과 2개의 예약 생성
        val fakeAmountPolicy = FakeAmountDiscountPolicy(2_000)
        val discountSystem = DiscountSystem(listOf(fakeAmountPolicy))

        val reservation1 = Reservation(screening, seats)
        val reservation2 = Reservation(screening, seats)
        val multipleReservations = Reservations(listOf(reservation1, reservation2))

        // when: 전체 할인된 금액을 계산
        val result = discountSystem.discountPrice(multipleReservations)

        // then: 각각 2,000원씩 할인되어 (16,000 * 2) = 32,000원이 반환
        assertThat(result).isEqualTo(Money(32_000))
    }

    @Test
    fun `정책 순서를 무작위로 주입해도 priority 순서에 맞게 할인이 적용된다`() {
        // given: 10% 정률 할인(우선순위 1)과 2000원 정액 할인(우선순위 2) 가짜 정책 생성
        val fakePercentPolicy = FakePercentDiscountPolicy(10, priority = 1)
        val fakeAmountPolicy = FakeAmountDiscountPolicy(2_000, priority = 2)

        // when: (우선순위가 낮은 정액 할인, 우선순위가 높은 정률 할인) 역순으로 시스템에 주입
        val discountSystem = DiscountSystem(listOf(fakeAmountPolicy, fakePercentPolicy))
        val result = discountSystem.discountPrice(reservations)

        // then: 우선순위 1(정률) 적용 후 우선순위 2(정액)가 적용되어야 함
        // 18,000 * 0.9(16,200) - 2,000 = 14,200
        assertThat(result).isEqualTo(Money(14_200))
    }
}