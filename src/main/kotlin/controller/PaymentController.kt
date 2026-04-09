package controller

import domain.purchase.Calculator
import domain.purchase.PaymentMethod
import domain.reservation.Cart
import domain.user.User
import view.InputView
import view.OutputView

class PaymentController(val cart: Cart, val user: User) {
    fun run(): Pair<Int, Int> {
        var price = discountPerSeat()
        val pair = getUserPoint(price)
        price = pair.first

        price = getPaymentMethod(price)

        OutputView.printTotalPrice(price)

        return price to pair.second
    }

    fun getUserPoint(totalPrice: Int): Pair<Int, Int> {
        val input = InputView.readPoint()
        user.discountPoint(input.toLong())

        val finalPrice = totalPrice - input.toInt()
        return finalPrice to input.toInt()
    }

    fun discountPerSeat(): Int {
        val discountPrice = cart.reservationInfos.map {
            val initPrice = it.seat.grade.price
            Calculator.calculateByMovie(
                price = initPrice,
                date = it.showing.startTime,
            )
        }
        return discountPrice.sum()
    }

    fun getPaymentMethod(price: Int): Int {
        val input = InputView.readPaymentMethod()

        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }

        val method = PaymentMethod.entries.first { (it.ordinal + 1) == input.toInt() }

        return Calculator.applyPaymentDiscount(price, method)
    }
}
