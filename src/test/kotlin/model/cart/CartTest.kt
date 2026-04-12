package model.cart

import model.discount.reserveDiscountPolicy.MovieDayDiscountPolicy
import model.discount.reserveDiscountPolicy.MovieDiscountPolicy
import model.discount.reserveDiscountPolicy.TimeDiscountPolicy
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

        val newCart = cart.addItem(CartItem(createScreening(), listOf("A1")))

        assertThat(newCart.items).hasSize(1)
    }

    @Test
    fun `여러 영화를 장바구니에 담을 수 있다`() {
        val cart = Cart()

        val newCart =
            cart
                .addItem(
                    CartItem(
                        createScreening(
                            startDateTime = LocalDateTime.of(2026, 4, 11, 10, 0),
                        ),
                        listOf("A1"),
                    ),
                ).addItem(
                    CartItem(
                        createScreening(
                            startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0),
                        ),
                        listOf("B2"),
                    ),
                )

        assertThat(newCart.items).hasSize(2)
    }

    @Test
    fun `장바구니에 담긴 모든 항목의 총 금액을 계산할 수 있다`() {
        val cart =
            Cart().addItem(
                CartItem(
                    createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)),
                    listOf("A1"),
                ),
            )

        val totalPrice =
            cart.calculateItemsPrice(
                reserveDiscountPolicy =
                    MovieDiscountPolicy(
                        movieDiscountPolicies =
                            listOf(
                                MovieDayDiscountPolicy(),
                                TimeDiscountPolicy(),
                            ),
                    ),
            )

        assertThat(totalPrice.value).isEqualTo(12_000)
    }

    @Test
    fun `총 금액 계산 시 항목별로 무비데이와 시간 할인이 각각 적용된다`() {
        val cart =
            Cart()
                .addItem(
                    CartItem(
                        createScreening(startDateTime = LocalDateTime.of(2026, 4, 10, 10, 0)),
                        listOf("A1"),
                    ),
                ).addItem(
                    CartItem(
                        createScreening(startDateTime = LocalDateTime.of(2026, 4, 11, 13, 0)),
                        listOf("C1"),
                    ),
                )

        val totalPrice =
            cart.calculateItemsPrice(
                reserveDiscountPolicy =
                    MovieDiscountPolicy(
                        movieDiscountPolicies =
                            listOf(
                                MovieDayDiscountPolicy(),
                                TimeDiscountPolicy(),
                            ),
                    ),
            )

        assertThat(totalPrice.value).isEqualTo(26_800)
    }

    private fun createScreening(
        startDateTime: LocalDateTime =
            LocalDateTime.of(
                2026,
                4,
                11,
                13,
                0,
            ),
    ): Screening =
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
