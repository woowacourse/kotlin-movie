package view

import domain.common.Money
import domain.payment.Point
import domain.screening.ScreeningSchedule
import domain.seat.Seats
import domain.ticket.TicketBucket
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object OutputView {
    fun createNewReservePrompt() {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
    }

    fun reserveMoviePrompt() {
        println("예매할 영화 제목을 입력하세요:")
    }

    fun reserveDatePrompt() {
        println("날짜를 입력하세요 (YYYY-MM-DD):")
    }

    fun selectMovieSchedulePrompt(schedule: ScreeningSchedule) {
        println("해당 날짜의 상영 목록")
        schedule.screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.startTime.toTimeString()}")
        }
        println("상영 번호를 선택하세요:")
    }

    fun selectSeatsPrompt(seats: Seats) {
        println("좌석을 선택하세요:")
        displaySeats(seats)
        println()
        println("예약할 좌석을 입력하세요 (A1, B2):")
    }

    fun addExtraMoviePrompt() {
        println("다른 영화를 추가하시겠습니까? (Y/N)")
    }

    fun displayAddedBucket(bucket: TicketBucket) {
        println("장바구니에 추가됨")
        bucket.tickets.forEach { ticket ->
            println(
                "- [${ticket.screening.movie.title}] ${ticket.screening.startTime.toUiString()} 좌석: ${
                    ticket.seatPositions.positions.joinToString {
                        "${it.row.value}${it.column.value}"
                    }
                }",
            )
        }
    }

    fun pointPrompt() {
        println("사용할 포인트를 입력하세요 (없으면 0):")
    }

    fun selectPaymentType() {
        println("결제 수단을 선택하세요:")
        println("1) 신용카드(5% 할인)")
        println("2) 현금(2% 할인")
    }

    fun decideToPayPrompt(totalPrice: Money) {
        println("가격 계산")
        println("최종 결제 금액: ${String.format("%,d", totalPrice.amount)}원")
        println("위 금액으로 결제하시겠습니까? (Y/N)")
    }

    fun displayResult(
        bucket: TicketBucket,
        totalPrice: Money,
        point: Point,
    ) {
        println("예매 완료\n내역:")
        bucket.tickets.forEach { ticket ->
            println(
                "- [${ticket.screening.movie.title}] ${ticket.screening.startTime.toUiString()} 좌석: ${
                    ticket.seatPositions.positions.joinToString(
                        " ",
                    ) { "${it.row.value}${it.column.value}" }
                }",
            )
        }
        println(
            "결제 금액: ${String.format("%,d", totalPrice.amount)}원 (포인트 ${
                String.format(
                    "%,d",
                    point.amount,
                )
            }원 사용)",
        )
        println("감사합니다.")
    }

    private fun displaySeats(seats: Seats) {
        val grouped = seats.seats.groupBy { it.position.row.value }
        val columns =
            seats.seats
                .map { it.position.column.value }
                .distinct()
                .sorted()

        println("좌석 배치도")
        println(columns.joinToString("") { "%5d".format(it) })
        grouped.entries.sortedBy { it.key }.forEach { (row, rowSeats) ->
            val rowLine =
                rowSeats
                    .sortedBy { it.position.column.value }
                    .joinToString("") { " [${it.grade.name}]" }
            println("$row$rowLine")
        }
    }

    fun displayError(message: String) {
        println(message)
    }

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun LocalDateTime.toUiString(): String = dateTimeFormatter.format(this)

    private fun LocalDateTime.toTimeString(): String = timeFormatter.format(this)
}
