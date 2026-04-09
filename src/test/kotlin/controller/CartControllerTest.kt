package controller

import domain.ReservationInfo
import domain.Seat
import domain.Showing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Cart(val reservationInfos: List<ReservationInfo>) {
    fun addInfo(info: ReservationInfo): Cart {
        return Cart(
            reservationInfos.plus(info),
        )
    }
}

class CartController {
    var cart: Cart = Cart(
        reservationInfos = listOf(),
    )

    fun run() {}

    fun addAllReservationInfo(
        showing: Showing,
        seats: List<Seat>,
    ) {
        seats.forEach { seat ->
            addReservationInfo(ReservationInfo(showing, seat))
        }
    }

    fun addReservationInfo(reservationInfo: ReservationInfo) {
        cart = cart.addInfo(reservationInfo)
    }
}

class CartControllerTest {
    val controller: CartController = CartController()

    @Test
    fun `장바구니에 예매 항목을 추가할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어진다.
        val showing = TestFixtureData.showings.first()
        val seats = listOf(TestFixtureData.seats[2], TestFixtureData.seats[3])

        // when : 장바구니에 예매 항목을 추가하면
        controller.addAllReservationInfo(
            showing = showing,
            seats = seats,
        )

        val result = controller.cart

        // then :
        assertEquals(2, result.reservationInfos.size)
    }
}
