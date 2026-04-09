package controller

import domain.cinema.Showing
import domain.reservation.Cart
import domain.reservation.ReservationInfo
import domain.seat.Seat
import view.OutputView

class CartController {
    var cart: Cart = Cart(
        reservationInfos = listOf(),
    )

    fun run(
        showing: Showing,
        seats: List<Seat>,
    ): Cart {
        addAllReservationInfo(showing, seats)
        showCart()
        return cart
    }

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
        OutputView.printCart(getAllReservationInfo())
    }
}
