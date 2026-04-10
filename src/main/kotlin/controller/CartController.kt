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
        cart.addInfos(
            ReservationInfo(
                showing = showing,
                seats = seats,
            ),
        )
        OutputView.printCart(cart)
        return cart
    }
}
