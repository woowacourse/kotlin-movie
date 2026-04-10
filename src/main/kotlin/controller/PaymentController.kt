package controller

import domain.cart.Cart
import domain.discount.MovieDayDiscount
import domain.discount.TimeDiscount
import domain.purchase.PaymentMethod
import domain.purchase.Price
import domain.user.Point
import domain.user.User
import view.InputView
import view.OutputView

class PaymentController(val cart: Cart, val user: User) {
    fun run(): Pair<Price, Point> {
        var price = discountPerSeat()
        val pair = getUserPoint(price)
        price = pair.first

        price = getPaymentMethod(price)

        OutputView.printTotalPrice(price.price)

        return price to pair.second
    }

    fun getUserPoint(totalPrice: Price): Pair<Price, Point> {
        val input = InputView.readPoint()
        user.discountPoint(input.toInt())

        val finalPrice = totalPrice.subtractPrice(input.toInt())
        return finalPrice to Point(input.toInt())
    }

    fun discountPerSeat(): Price {
        val discountPrice =
            cart.reservationInfos.infos.map {
                var initPrice = it.seats.getAllPrice()

                val movieDayDiscount = MovieDayDiscount()
                if (movieDayDiscount.isApplicable(it.showing.startTime)) {
                    initPrice = movieDayDiscount.discountApply(initPrice)
                }

                val timeDiscount = TimeDiscount()
                if (timeDiscount.isApplicable(it.showing.startTime)) {
                    initPrice = timeDiscount.discountApply(initPrice)
                }
                initPrice.price
            }
        return Price(discountPrice.sum())
    }

    fun getPaymentMethod(price: Price): Price {
        val input = InputView.readPaymentMethod()

        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }

        val method = PaymentMethod.entries.first { (it.ordinal + 1) == input.toInt() }

        return method.discountApply(price)
    }
}
