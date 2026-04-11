package view

import domain.cart.Cart
import domain.cinema.Screen
import domain.cinema.Showings
import domain.purchase.Price
import domain.user.Point

object OutputView {
    fun printShowing(showings: Showings) {
        println("해당 날짜의 상영 목록")

        showings.showings.forEachIndexed { index, showing ->
            println("[${index + 1}] ${showing.startTime.time}")
        }
    }

    fun printSeats(screen: Screen) {

        println("좌석 배치도")
        val header = " ".repeat(3) + (1..Screen.MAX_COLUMN).joinToString("    ")
        println(header)
        printSeatRows(screen)
        println()
    }

    fun printSeatRows(screen: Screen) {
        ('A' until 'A' + Screen.MAX_ROW).forEach { row ->
            val line = "$row" + (1..Screen.MAX_COLUMN).joinToString("") { col ->
                val seat = screen.seats.seats.find { it.coordinate.row == row && it.coordinate.column == col }
                " [ ${seat?.grade?.name ?: " "}]"
            }
            println(line)
        }
    }

    fun printCart(items: List<String>) {
        println("장바구니")
        println(items)
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
        totalPrice: Price,
        usedPoint: Point,
    ) {
        println("예매완료")
        println("내역:")
        printCart(cart.showItems())

        println("결제 금액: ${printByDecimalFormat(totalPrice.price)}원 (포인트 ${printByDecimalFormat(usedPoint.point)})")

        println()
        println("감사합니다.")
    }
}
