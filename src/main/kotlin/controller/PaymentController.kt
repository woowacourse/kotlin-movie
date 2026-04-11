package controller

import domain.purchase.Payment
import domain.purchase.PaymentMethod
import domain.purchase.PaymentResult
import domain.user.Point
import view.InputView
import view.OutputView
import view.retryUntilValid

class PaymentController {
    fun run(payment: Payment): PaymentResult {
        val point = retryUntilValid { Point(InputView.readPoint().toInt()) }
        val method = retryUntilValid { PaymentMethod.from(getPaymentMethod()) }
        val result = retryUntilValid { payment.calculate(point, method) }
        OutputView.printTotalPrice(result.totalPrice.price)
        return result
    }

    fun getPaymentMethod(): Int {
        val input = InputView.readPaymentMethod()
        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }
        return input.toInt()
    }
}
