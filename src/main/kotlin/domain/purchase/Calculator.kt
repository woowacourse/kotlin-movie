package domain.purchase

import kotlinx.datetime.LocalDateTime

object Calculator {

    fun calculateByMovie(
        price: Int,
        date: LocalDateTime,
    ): Int {
        var discountedPrice = applyMovieDayDiscount(price, date)
        discountedPrice = applyTimeDiscount(discountedPrice, date)

        return discountedPrice
    }

    fun applyMovieDayDiscount(
        price: Int,
        date: LocalDateTime,
    ): Int {
        return ((1 - DiscountPolicy.movieDayDiscount(date)) * price).toInt()
    }

    fun applyTimeDiscount(
        price: Int,
        date: LocalDateTime,
    ): Int {
        return price - DiscountPolicy.showTimeDiscount(date)
    }

    fun applyPaymentDiscount(
        price: Int,
        method: PaymentMethod,
    ): Int {
        return ((1 - DiscountPolicy.paymentDiscount(method)) * price).toInt()
    }
}
