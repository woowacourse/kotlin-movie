package controller

import domain.Money
import domain.PaymentSystem
import domain.PaymentType
import domain.Point
import domain.Screening
import domain.ScreeningSchedule
import domain.TicketBucket
import domain.Title
import view.InputView
import view.OutputView
import java.time.LocalDate

class Controller(
    val schedule: ScreeningSchedule,
) {
    val paymentSystem = PaymentSystem()

    fun run() {
        if (!startReservation()) return
        var ticketBucket = TicketBucket()

        do {
            val movieTitle = getMovie()
            val reserveDate = getReserveDate()
            val movieSchedule = schedule.getMovieSchedule(movieTitle, reserveDate)

            val selectedScreening = selectMovieScreening(movieSchedule)
            ticketBucket = selectSeats(ticketBucket, selectedScreening)
            OutputView.displayAddedBucket(ticketBucket)
        } while (confirmAddExtraMovie())

        val point = getPoint()
        val payment = getPaymentType()

        val totalPrice = paymentSystem.calculate(point, payment, ticketBucket)

        if (!confirmPurchase(totalPrice)) return

        schedule.reserve(bucket = ticketBucket)

        OutputView.displayResult(ticketBucket, totalPrice, point)
    }

    private fun startReservation(): Boolean =
        retryUntilValid {
            OutputView.createNewReservePrompt()
            val input = InputView.read()
            InputParser.parseYN(input)
        }

    private fun getMovie(): Title =
        retryUntilValid {
            OutputView.reserveMoviePrompt()
            val input = InputView.read()
            InputParser.parseMovieTitle(input)
        }

    private fun getReserveDate(): LocalDate =
        retryUntilValid {
            OutputView.reserveDatePrompt()
            val input = InputView.read()
            InputParser.parseDate(input)
        }

    private fun selectMovieScreening(movieSchedule: ScreeningSchedule): Screening =
        retryUntilValid {
            OutputView.selectMovieSchedulePrompt(movieSchedule)
            val input = InputView.read()
            val select = InputParser.parseNum(input)
            movieSchedule.screenings[select - 1]
        }

    private fun selectSeats(
        ticketBucket: TicketBucket,
        screening: Screening,
    ): TicketBucket =
        retryUntilValid {
            OutputView.selectSeatsPrompt(screening.seats)
            val input = InputView.read()
            val selectedSeats = InputParser.parseSeats(input)
            screening.canReserve(selectedSeats)

            ticketBucket.addTicket(screening, selectedSeats)
        }

    private fun confirmAddExtraMovie(): Boolean =
        retryUntilValid {
            OutputView.addExtraMoviePrompt()
            val input = InputView.read()
            InputParser.parseYN(input)
        }

    private fun getPoint(): Point =
        retryUntilValid {
            OutputView.pointPrompt()
            val input = InputView.read()
            InputParser.parsePoint(input)
        }

    private fun getPaymentType(): PaymentType =
        retryUntilValid {
            OutputView.selectPaymentType()
            val input = InputView.read()
            InputParser.parsePaymentType(input)
        }

    private fun confirmPurchase(totalPrice: Money): Boolean =
        retryUntilValid {
            OutputView.decideToPayPrompt(totalPrice)
            val input = InputView.read()
            InputParser.parseYN(input)
        }

    private inline fun <T> retryUntilValid(block: () -> T): T {
        while (true) {
            try {
                return block()
            } catch (e: IllegalArgumentException) {
                OutputView.displayError(e.message ?: "알 수 없는 에러 발생!! 삐용삐용")
            }
        }
    }
}
