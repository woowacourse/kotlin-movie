package model.cart

import model.discount.DiscountBenefits
import model.discount.PaymentMethod
import model.movie.Movie
import model.schedule.Screening
import model.seat.SeatInventory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class CartTest {
    @Test
    fun `장바구니에 예매 항목을 추가할 수 있다`() {
        val cart = Cart()

        val newCart = cart.addItem(createScreening(), listOf("A1"))

        assertThat(newCart.items).hasSize(1)
    }

    @Test
    fun `여러 영화를 장바구니에 담을 수 있다`() {
        val cart = Cart()

        val newCart =
            cart
                .addItem(createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 10, 0)), listOf("A1"))
                .addItem(createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)), listOf("B2"))

        assertThat(newCart.items).hasSize(2)
    }

    @Test
    fun `장바구니에 담긴 모든 항목의 총 금액을 계산할 수 있다`() {
        val cart =
            Cart().addItem(
                createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)),
                listOf("A1"),
            )

        val totalPrice =
            cart.calculateTotalPrice(
                discountBenefits = DiscountBenefits(),
                usePoint = 0,
                paymentMethod = PaymentMethod.CASH,
            )

        assertThat(totalPrice).isEqualTo(11_760)
    }

    @Test
    fun `총 금액 계산 시 항목별로 무비데이와 시간 할인이 각각 적용된다`() {
        val cart =
            Cart()
                .addItem(
                    createScreening(startDateTime = LocalDateTime.of(2026, 4, 10, 10, 0)),
                    listOf("A1"),
                ).addItem(
                    createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)),
                    listOf("C1"),
                )

        val totalPrice =
            cart.calculateTotalPrice(
                discountBenefits = DiscountBenefits(),
                usePoint = 0,
                paymentMethod = PaymentMethod.CARD,
            )

        assertThat(totalPrice).isEqualTo(25_460)
    }

    @Test
    fun `총 금액 계산 시 포인트 할인은 합산 금액에 한 번만 적용된다`() {
        val screening = createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0))
        val cart =
            Cart()
                .addItem(screening, listOf("A1"))
                .addItem(screening, listOf("A2"))

        val totalPrice =
            cart.calculateTotalPrice(
                discountBenefits = DiscountBenefits(),
                usePoint = 4_000,
                paymentMethod = PaymentMethod.CASH,
            )

        assertThat(totalPrice).isEqualTo(19_600)
    }

    @Test
    fun `총 금액 계산 시 결제 수단 할인은 포인트 할인 후 금액에 적용된다`() {
        val cart =
            Cart().addItem(
                createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)),
                listOf("A1"),
            )

        val totalPrice =
            cart.calculateTotalPrice(
                discountBenefits = DiscountBenefits(),
                usePoint = 2_000,
                paymentMethod = PaymentMethod.CARD,
            )

        assertThat(totalPrice).isEqualTo(9_500)
    }

    private fun createScreening(startDateTime: LocalDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)): Screening =
        Screening(
            movie =
                Movie(
                    movieTitle = "인터스텔라",
                    movieRunningTime = 130,
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
            startDateTime = startDateTime,
            seatInventory = SeatInventory.createDefaultSeatInventory(),
        )
}
