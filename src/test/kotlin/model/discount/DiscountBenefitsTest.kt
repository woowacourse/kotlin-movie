package model.discount

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class DiscountBenefitsTest {
    @Test
    fun `매월 10, 20, 30일에 상영되는 영화는 10% 할인된다`() {
        val price = 24_000
        val reservedDate = LocalDate.of(2026, 4, 10)
        val discountBenefits = DiscountBenefits()

        val movieDayDiscountPrice = discountBenefits.movieDay(price, reservedDate)

        assertThat(movieDayDiscountPrice).isEqualTo(21_600)
    }

    @Test
    fun `매월 10, 20, 30일에 상영되는 영화가 아니면 할인이 안된다`() {
        val price = 24_000
        val reservedDate = LocalDate.of(2026, 4, 11)
        val discountBenefits = DiscountBenefits()

        val movieDayDiscountPrice = discountBenefits.movieDay(price, reservedDate)

        assertThat(movieDayDiscountPrice).isEqualTo(24_000)
    }

    @Test
    fun `오전 11시 이전 또는 오후 8시 이후에 시작하는 영화는 2_000원 할인된다`() {
        val price = 24_000
        val earlyTime = LocalTime.of(10, 40)
        val lateTime = LocalTime.of(20, 10)
        val discountBenefits = DiscountBenefits()

        assertThat(discountBenefits.timeDiscount(price, earlyTime)).isEqualTo(22_000)
        assertThat(discountBenefits.timeDiscount(price, lateTime)).isEqualTo(22_000)
    }

    @Test
    fun `오전 11시와 오후 8시 사이에 시작하는 영화는 할인되지 않는다`() {
        val price = 24_000
        val reservedTime = LocalTime.of(11, 40)
        val discountBenefits = DiscountBenefits()

        val timeDiscountPrice = discountBenefits.timeDiscount(price, reservedTime)

        assertThat(timeDiscountPrice).isEqualTo(24_000)
    }

    @Test
    fun `두 가지의 할인혜택은 동시에 적용될 수 있다`() {
        val price = 18_000
        val movieDayDate = LocalDate.of(2026, 4, 10)
        val earlyTime = LocalTime.of(10, 0)
        val discountBenefits = DiscountBenefits()

        val result = discountBenefits.timeDiscount(
            discountBenefits.movieDay(price, movieDayDate),
            earlyTime,
        )

        assertThat(result).isEqualTo(14_200)
    }

    @Test
    fun `두 가지의 할인혜택이 적용될 때에는 퍼센트 할인이 먼저 적용된다`() {
        val price = 18_000
        val movieDayDate = LocalDate.of(2026, 4, 10)
        val earlyTime = LocalTime.of(10, 0)
        val discountBenefits = DiscountBenefits()

        val percentFirst = discountBenefits.timeDiscount(
            discountBenefits.movieDay(price, movieDayDate),
            earlyTime,
        )

        val fixedFirst = discountBenefits.movieDay(
            discountBenefits.timeDiscount(price, earlyTime),
            movieDayDate,
        )

        assertThat(percentFirst).isEqualTo(14_200)
        assertThat(fixedFirst).isEqualTo(14_400)
        assertThat(percentFirst).isNotEqualTo(fixedFirst)
    }

    @Test
    fun `포인트 할인 시 예매 금액에서 포인트만큼의 금액이 차감된다`() {
        val price = 24_000
        val usePoint = 2_000
        val discountBenefits = DiscountBenefits()

        val pointDiscount = discountBenefits.pointDiscount(price, usePoint)

        assertThat(pointDiscount).isEqualTo(22_000)
    }

    @Test
    fun `포인트는 예매 금액보다 크게 사용할 수 없다`() {
        val price = 24_000
        val usePoint = 25_000
        val discountBenefits = DiscountBenefits()

        assertThrows<IllegalArgumentException> {
            discountBenefits.pointDiscount(price, usePoint)
        }
    }

    @Test
    fun `신용카드로 결제 시 5% 할인된다`() {
        val price = 24_000
        val discountBenefits = DiscountBenefits()

        val cardDiscount = discountBenefits.cardDiscount(price)

        assertThat(cardDiscount).isEqualTo(22_800)
    }

    @Test
    fun `현금으로 결제 시 2% 할인된다`() {
        val price = 24_000
        val discountBenefits = DiscountBenefits()

        val cashDiscount = discountBenefits.cashDiscount(price)

        assertThat(cashDiscount).isEqualTo(23_520)
    }

    @Test
    fun `결제방식 할인은 포인트 할인이 적용된 후에 적용된다`() {
        val price = 24_000
        val usePoint = 4_000
        val discountBenefits = DiscountBenefits()

        val pointFirst = discountBenefits.cardDiscount(
            discountBenefits.pointDiscount(price, usePoint),
        )

        val cardFirst = discountBenefits.pointDiscount(
            discountBenefits.cardDiscount(price),
            usePoint,
        )

        assertThat(pointFirst).isEqualTo(19_000)
        assertThat(cardFirst).isEqualTo(18_800)
        assertThat(pointFirst).isNotEqualTo(cardFirst)
    }
}
