package controller

import domain.cart.Cart
import domain.purchase.Payment
import domain.purchase.PaymentMethod
import domain.purchase.PaymentResult
import domain.user.Point
import domain.user.User
import view.InputView
import view.OutputView

class PaymentController(val cart: Cart, val user: User) {
    fun run(payment: Payment): PaymentResult {
        val point = Point(InputView.readPoint().toInt())
        val method = PaymentMethod.from(getPaymentMethod())
        val result = payment.calculate(point, method)
        OutputView.printTotalPrice(result.totalPrice.price)
        return result
    }

    fun getPaymentMethod(): Int {
        val input = InputView.readPaymentMethod()
        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }
        return input.toInt()
    }
}
