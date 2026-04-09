package controller

import domain.Calculator
import domain.Cart
import domain.PaymentMethod
import domain.User
import view.InputView
import view.OutputView

class PaymentController(val cart: Cart, val user: User) {
    fun run(): Int {
        var price = discountPerSeat()
        price = getUserPoint(price)
        price = getPaymentMethod(price)

        OutputView.printTotalPrice(price)

        val input = InputView.readPurchaseConfirm()

        return price
    }

    fun getUserPoint(totalPrice: Int): Int {
        val input = InputView.readPoint()
        user.discountPoint(input.toLong())

        val finalPrice = totalPrice - input.toInt()
        return finalPrice
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
