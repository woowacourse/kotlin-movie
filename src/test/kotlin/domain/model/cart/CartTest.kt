package domain.model.cart

import domain.model.Movie
import domain.model.screen.Screening
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class CartTest {
    private fun movie(
        title: String = "테스트 영화",
        runningMinutes: Int = 120,
    ): Movie = Movie(title = title, runningMinutes = runningMinutes)

    private fun screening(
        date: LocalDate = LocalDate.of(2026, 4, 10),
        startTime: LocalTime = LocalTime.of(10, 0),
        title: String = "테스트 영화",
        runningMinutes: Int = 120,
    ): Screening =
        Screening(
            screeningDate = date,
            startTime = startTime,
            movie = movie(title, runningMinutes),
        )

    @Test
    fun `장바구니에 항목을 추가하면 items로 조회할 수 있다`() {
        val cart = Cart()
        val item =
            CartItem(
                screening = screening(),
                seatCodes = listOf("C2", "C3"),
            )

        cart.add(item)

        assertThat(cart.items()).containsExactly(item)
    }

    @Test
    fun `기존 장바구니 상영과 시간이 겹치면 hasOverlapping은 true를 반환한다`() {
        val cart = Cart()
        val reserved =
            CartItem(
                screening =
                    screening(
                        date = LocalDate.of(2026, 4, 10),
                        startTime = LocalTime.of(10, 0),
                        runningMinutes = 120,
                    ),
                seatCodes = listOf("A1"),
            )
        cart.add(reserved)

        val candidate =
            screening(
                date = LocalDate.of(2026, 4, 10),
                startTime = LocalTime.of(11, 0),
                runningMinutes = 90,
            )

        val result = cart.hasOverlapping(candidate)

        assertThat(result).isTrue()
    }

    @Test
    fun `기존 장바구니 상영과 시간이 겹치지 않으면 hasOverlapping은 false를 반환한다`() {
        val cart = Cart()
        val reserved =
            CartItem(
                screening =
                    screening(
                        date = LocalDate.of(2026, 4, 10),
                        startTime = LocalTime.of(10, 0),
                        runningMinutes = 120,
                    ),
                seatCodes = listOf("A1"),
            )
        cart.add(reserved)

        val candidate =
            screening(
                date = LocalDate.of(2026, 4, 10),
                startTime = LocalTime.of(12, 0),
                runningMinutes = 90,
            )

        val result = cart.hasOverlapping(candidate)

        assertThat(result).isFalse()
    }
}
