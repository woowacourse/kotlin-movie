package console.controller

import domain.common.Money
import domain.discount.MoviedayDiscount
import domain.discount.PaymentDiscount
import domain.discount.TicketDiscountPolicy
import domain.discount.TimeDiscount
import domain.discount.TotalDiscountPolicy
import domain.movie.Title
import domain.payment.PaymentSystem
import domain.payment.PaymentType
import domain.common.Point
import domain.screening.Screening
import domain.screening.ScreeningSchedule
import domain.ticket.Ticket
import domain.ticket.TicketBucket
import console.view.InputView
import console.view.OutputView
import java.time.LocalDate

class Controller(
    private var schedule: ScreeningSchedule,
) {
    private val paymentSystem = PaymentSystem(
        ticketDiscountStrategy = TicketDiscountPolicy(
            strategies = listOf(
                MoviedayDiscount(),
                TimeDiscount(),
            )
        ),
        totalDiscountStrategy = TotalDiscountPolicy(
            strategies = listOf(
                PaymentDiscount(),
            )
        )
    )

    fun run() {
        if (!startReservation()) return
        var ticketBucket = TicketBucket()

        do {
            val movieTitle = getMovie()
            val reserveDate = getReserveDate()
            val movieSchedule = schedule.getMovieSchedule(movieTitle, reserveDate)

            val selectedScreening = selectMovieScreening(ticketBucket, movieSchedule)
            ticketBucket = selectSeats(ticketBucket, selectedScreening)
            OutputView.displayAddedBucket(ticketBucket)
        } while (confirmAddExtraMovie())

        val point = getPoint()
        val payment = getPaymentType()

        val totalPrice = paymentSystem.calculate(point, payment, ticketBucket)

        if (!confirmPurchase(totalPrice)) return

        schedule = schedule.reserve(bucket = ticketBucket)

        OutputView.displayResult(ticketBucket, totalPrice, point)
    }

    private fun startReservation(): Boolean =
        retryUntilValid {
            OutputView.createNewReservePrompt()
            InputView.readYN()
        }

    private fun getMovie(): Title =
        retryUntilValid {
            OutputView.reserveMoviePrompt()
            InputView.readMovieTitle()
        }

    private fun getReserveDate(): LocalDate =
        retryUntilValid {
            OutputView.reserveDatePrompt()
            InputView.readDate()
        }

    private fun selectMovieScreening(
        ticketBucket: TicketBucket,
        movieSchedule: ScreeningSchedule,
    ): Screening =
        retryUntilValid {
            OutputView.selectMovieSchedulePrompt(movieSchedule)
            val select = InputView.readNum()
            val selected = movieSchedule.screenings[select - 1]
            ticketBucket.validateSchedulable(selected)
            return selected
        }

    private fun selectSeats(
        ticketBucket: TicketBucket,
        screening: Screening,
    ): TicketBucket =
        retryUntilValid {
            OutputView.selectSeatsPrompt(screening.seats)
            val selectedSeats = InputView.readSeats()
            screening.isReservable(selectedSeats)

            ticketBucket.addTicket(Ticket(screening, selectedSeats))
        }

    private fun confirmAddExtraMovie(): Boolean =
        retryUntilValid {
            OutputView.addExtraMoviePrompt()
            InputView.readYN()
        }

    private fun getPoint(): Point =
        retryUntilValid {
            OutputView.pointPrompt()
            InputView.readPoint()
        }

    private fun getPaymentType(): PaymentType =
        retryUntilValid {
            OutputView.selectPaymentType()
            InputView.readPaymentType()
        }

    private fun confirmPurchase(totalPrice: Money): Boolean =
        retryUntilValid {
            OutputView.decideToPayPrompt(totalPrice)
            InputView.readYN()
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
