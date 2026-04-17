package console.view

import model.cart.Cart
import model.cart.CartItem
import model.schedule.Screening
import model.seat.SeatInventory
import model.seat.SeatRank
import java.time.format.DateTimeFormatter

class OutputView {
    fun printErrorMessage(message: String) {
        println("\n$message")
    }

    fun printScreenings(screenings: List<Screening>) {
        println("\n해당 날짜의 상영 목록")
        screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
        }
    }

    fun printSeatInventory(seatInventory: SeatInventory) {
        println("\n좌석 배치도")
        val columns = SeatInventory.columns
        val rows = SeatInventory.rows

        print("  ")
        columns.forEach { print("  $it  ") }
        println()

        rows.forEach { row ->
            print("$row ")
            columns.forEach { col ->
                val seat =
                    seatInventory.getSeat("$row$col")
                val label =
                    when (seat.seatRank) {
                        SeatRank.S_RANK -> "S"
                        SeatRank.A_RANK -> "A"
                        SeatRank.B_RANK -> "B"
                    }
                print("[ $label]")
            }
            println()
        }
    }

    fun printCartItemAdded(cartItem: CartItem) {
        println("\n장바구니에 추가됨")
        println(formatCartItem(cartItem))
    }

    fun printCart(cart: Cart) {
        println("\n장바구니")
        cart.items.forEach {
            println(formatCartItem(it))
        }
    }

    fun printTotalPrice(totalPrice: Int) {
        println("\n가격 계산")
        println("최종 결제 금액: ${"%,d".format(totalPrice)}원")
    }

    fun printReservationComplete(
        cart: Cart,
        totalPrice: Int,
        usePoint: Int,
    ) {
        println("\n예매 완료")
        println("내역:")
        cart.items.forEach { println(formatCartItem(it)) }
        if (usePoint > 0) {
            println("결제 금액: ${"%,d".format(totalPrice)}원  (포인트 ${"%,d".format(usePoint)}원 사용)")
        } else {
            println("결제 금액:${"%,d".format(totalPrice)}원")
        }
        println("\n감사합니다.")
    }

    private fun formatCartItem(cartItem: CartItem): String {
        val title = cartItem.screening.movie.title
        val date = cartItem.screening.startDateTime.toLocalDate()
        val time = cartItem.screening.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val seats = cartItem.seatNames.joinToString(", ")
        return "- [$title] $date $time  좌석: $seats"
    }
}
