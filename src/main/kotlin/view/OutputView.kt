package view

import domain.cart.Cart
import domain.cinema.Screen
import domain.cinema.Showing
import kotlin.collections.component1
import kotlin.collections.component2

object OutputView {
    fun printShowing(showings: List<Showing>) {
        println("해당 날짜의 상영 목록")

        showings.forEachIndexed { index, showing ->
            println("[${index + 1}] ${showing.startTime.time}")
        }
    }

    fun printSeats(screen: Screen) {
        val maxRow = Screen.MAX_ROW
        val maxColumn = Screen.MAX_COLUMN

        println("좌석 배치도")
        val header = " ".repeat(3) + (1..maxColumn).joinToString("    ")
        println(header)

        ('A' until 'A' + maxRow).forEach { row ->
            val line = "$row" + (1..maxColumn).joinToString("") { col ->
                val seat = screen.seats.seats.find { it.coordinate.row == row && it.coordinate.column == col }
                " [ ${seat?.grade?.name ?: " "}]"
            }
            println(line)
        }
        println()
    }

    fun printCart(cart: Cart) {
        println("장바구니")
        println(cart.showItems())
        println()
    }

    fun printTotalPrice(price: Int) {
        println("가격 계산")
        println("최종 결제 금액: ${printByDecimalFormat(price)}원")
        println()
    }

    fun printByDecimalFormat(price: Int): String {
        return String.format("%,d", price)
    }

    fun printTotal(
        cart: Cart,
        totalPrice: Int,
        usedPoint: Int,
    ) {
        println("예매완료")
        println("내역:")
        printCart(cart)

        println("결제 금액: ${printByDecimalFormat(totalPrice)}원 (포인트 ${printByDecimalFormat(usedPoint)})")

        println()
        println("감사합니다.")
    }
}
