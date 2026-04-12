package view

import model.cart.Cart
import model.cart.CartItem
import model.schedule.Screening
import model.seat.SeatInventory
import model.seat.SeatRank
import java.time.format.DateTimeFormatter

class OutputView {
    // 에러 메세지 출력 함수
    fun printErrorMessage(message: String) {
        println("\n$message")
    }

    // 해당 날짜의 상영 목록 출력 함수
    fun printScreenings(screenings: List<Screening>) {
        println("\n해당 날짜의 상영 목록")
        screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
        }
    }

    // 좌석 배치도를 출력하는 함수
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
                    seatInventory.findSeat("$row$col")
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

    // 장바구니에 추가됨을 출력하는 함수
    fun printCartItemAdded(cartItem: CartItem) {
        println("\n장바구니에 추가됨")
        println(formatCartItem(cartItem))
    }

    // 장바구니에 담겨있는 목록을 출력하는 함수
    fun printCart(cart: Cart) {
        println("\n장바구니")
        cart.items.forEach {
            println(formatCartItem(it))
        }
    }

    // 결제 금액을 출력하는 함수
    fun printTotalPrice(totalPrice: Int) {
        println("\n가격 계산")
        println("최종 결제 금액: ${"%,d".format(totalPrice)}원")
    }

    // 할인 적용 포함 결제 내역을 출력하는 함수
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
        val title = cartItem.screening.movie.movieTitle
        val date = cartItem.screening.startDateTime.toLocalDate()
        val time = cartItem.screening.startDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val seats = cartItem.seatNames.joinToString(", ")
        return "- [$title] $date $time  좌석: $seats"
    }
}
