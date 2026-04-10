package controller

import domain.cart.Cart
import domain.cinema.Showing
import domain.reservation.ReservationInfo
import domain.seat.Seats
import view.OutputView

class CartController {
    fun run(
        cart: Cart,
        showing: Showing,
        seats: Seats,
    ): Cart {
        addReservationInfo(cart, showing, seats)
        OutputView.printCart(cart)
        return cart
    }

    fun addReservationInfo(
        cart: Cart,
        showing: Showing,
        seats: Seats,
    ): Cart {

        return cart.addInfos(
            ReservationInfo(
                showing = showing,
                seats = seats,
            ),
        )
    }
}
