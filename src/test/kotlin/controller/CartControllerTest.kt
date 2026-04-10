package controller

import domain.cart.Cart
import domain.reservation.ReservationInfo
import domain.reservation.ReservationInfos
import domain.seat.Seats
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CartControllerTest {
    val controller: CartController = CartController()

    @Test
    fun `장바구니에 예매 항목을 추가할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어진다.
        val showing = TestFixtureData.showings.first()
        val seats = Seats(listOf(TestFixtureData.seats.seats[2], TestFixtureData.seats.seats[3]))
        val cart = Cart(ReservationInfos(emptyList()))

        // when : 장바구니에 예매 항목을 추가하면
        val result = cart.addInfos(
            ReservationInfo(
                showing = showing,
                seats = seats,
            ),
        )

        // then :
        assertEquals(2, result.reservationInfos.infos.first().seats.seats.size)
    }

    @Test
    fun `장바구니에 담긴 전체 항목을 조회할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어지고 장바구니에 예매 항목을 추가한다.
        val showing = TestFixtureData.showings.first()
        val seats = Seats(listOf(TestFixtureData.seats.seats[2], TestFixtureData.seats.seats[3]))
        val cart = Cart(ReservationInfos(emptyList()))

        // when : 장바구니에 담긴 전체 항목을 조회하면
        val result = cart.addInfos(
            ReservationInfo(
                showing = showing,
                seats = seats,
            ),
        )

        val actual = result.reservationInfos.getAllInfos()

        // then : 전체 항목이 반환된다.
        assertEquals(listOf("- [해리 포터] 2026-04-10 10:00 좌석: B1, B2"), actual)
    }
}
