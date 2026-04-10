package controller

import domain.cart.Cart
import domain.purchase.PaymentMethod
import domain.purchase.PaymentResult
import domain.purchase.Price
import domain.user.Point
import domain.user.User
import view.InputView
import view.OutputView

class PaymentController(val cart: Cart, val user: User) {
    fun run(): PaymentResult {
        var price = discountPerSeat()
        val pointResult = getUserPoint(price)
        price = pointResult.totalPrice

        price = getPaymentMethod(price)

        OutputView.printTotalPrice(price.price)

        return PaymentResult(price, pointResult.usedPoint)
    }

    fun getUserPoint(totalPrice: Price): PaymentResult {
        val input = InputView.readPoint()
        user.discountPoint(input.toInt())

        val finalPrice = totalPrice.subtractPrice(input.toInt())
        return PaymentResult(finalPrice, Point(input.toInt()))
    }

    fun discountPerSeat(): Price {
        val discountPrice =
            cart.getDiscountedItems()
        return Price(discountPrice)
    }

    fun getPaymentMethod(price: Price): Price {
        val input = InputView.readPaymentMethod()
        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }

        val method = PaymentMethod.from(input.toInt())
        return method.discountApply(price)
    }
}
