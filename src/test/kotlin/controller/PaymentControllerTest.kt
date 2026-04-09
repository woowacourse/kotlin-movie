package controller

import domain.Calculator
import domain.Cart
import domain.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PaymentController(val cart: Cart, val user: User) {

    var totalPrice: Int = cart.getTotalPrice()
    fun run() {
    }

    fun getUserPoint() {
        println("사용할 포인트를 입력하세요 (없으면 0):")
        val input = readln()

        user.discountPoint(input.toLong())

        totalPrice -= input.toInt()
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
}

class PaymentControllerTest {

    val controller = PaymentController(
        cart = TestFixtureData.cart,
        user = TestFixtureData.users.first(),
    )

    @Test
    fun `좌석 별로 무비데이 할인(10%)과 시간 할인(2,000원)이 적용된다`() {
        // given & when : 무비데이 할인과 시간 할인을 적용한다.
        val result = controller.discountPerSeat()

        // then : 할인된 금액이 반환된다.
        assertEquals(27700, result)
    }
}
