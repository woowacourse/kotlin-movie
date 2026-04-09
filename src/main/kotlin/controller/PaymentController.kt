package controller

import domain.Calculator
import domain.Cart
import domain.PaymentMethod
import domain.User

class PaymentController(val cart: Cart, val user: User) {
    fun run(): Int {
        var price = discountPerSeat()
        price = getUserPoint(price)
        price = getPaymentMethod(price)

        println("가격 계산")
        println("최종 결제 금액: ${price}원")

        println("위 금액으로 결제하시겠습니까? (Y/N)")
        val input = readln()

        return price
    }

    fun getUserPoint(totalPrice: Int): Int {
        println("사용할 포인트를 입력하세요 (없으면 0):")
        val input = readln()

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
        println(
            """
            결제 수단을 선택하세요:
            1) 신용카드(5% 할인)
            2) 현금(2% 할인)
            """.trimIndent(),
        )
        val input = readln()

        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }

        val method = PaymentMethod.entries.first { it.ordinal == input.toInt() }

        return Calculator.applyPaymentDiscount(price, method)
    }
}
