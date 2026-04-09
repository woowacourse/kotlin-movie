package controller

import domain.Cart
import domain.ReservationInfo
import domain.Seat
import domain.Showing

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
