package controller

import domain.cart.Cart
import domain.reservation.ReservationInfo
import view.OutputView

class CartController {
    fun run(
        cart: Cart,
        info: ReservationInfo,
    ): Cart {
        cart.addInfos(info)
        OutputView.printCart(cart.showItems())
        return cart
    }
}
