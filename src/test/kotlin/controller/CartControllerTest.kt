package controller

import domain.ReservationInfo
import domain.Seat
import domain.Showing
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
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

    fun getAllReservationInfo(): List<String> {

        val format = LocalDateTime.Format {
            year()
            char('-')
            monthNumber(Padding.ZERO)
            char('-')
            dayOfMonth(Padding.ZERO)
            char(' ')
            hour()
            char(':')
            minute()
        }

        val carts = cart.reservationInfos.groupBy { it.showing }
            .map { (key, group) ->
                val showing = key
                val seats = group.joinToString(", ") { "${it.seat.row}${it.seat.column}" }
                "- [${showing.movie.title}] ${
                    showing.startTime.toString().replace("T", " ").substring(0, 16)
                } 좌석: $seats"
            }

        return carts
    }

    fun showCart() {
        println("장바구니")
        getAllReservationInfo().forEach {
            println(it)
        }
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

    @Test
    fun `장바구니에 담긴 전체 항목을 조회할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어지고 장바구니에 예매 항목을 추가한다.
        val showing = TestFixtureData.showings.first()
        val seats = listOf(TestFixtureData.seats[2], TestFixtureData.seats[3])

        controller.addAllReservationInfo(
            showing = showing,
            seats = seats,
        )

        // when : 장바구니에 담긴 전체 항목을 조회하면
        val result = controller.getAllReservationInfo()

        // then : 전체 항목이 반환된다.
        assertEquals(listOf("- [해리 포터] 2026-04-10 10:00 좌석: B1, B2"), result)
    }
}
