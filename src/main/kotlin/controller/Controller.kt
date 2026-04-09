package controller

import domain.Column
import domain.Money
import domain.Movie
import domain.PaymentSystem
import domain.PaymentType
import domain.Point
import domain.Row
import domain.RunningTime
import domain.Screening
import domain.ScreeningPeriod
import domain.ScreeningRoom
import domain.ScreeningRoomName
import domain.ScreeningSchedule
import domain.Seat
import domain.SeatPosition
import domain.Seats
import domain.Ticket
import domain.TicketBucket
import domain.TimeRange
import domain.Title
import view.InputView
import view.OutputView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Controller {
    val schedule =
        ScreeningSchedule(
            screenings =
                listOf(
                    Screening(
                        movie =
                            Movie(
                                title = Title("허닛"),
                                runningTime = RunningTime(167),
                                screeningPeriod =
                                    ScreeningPeriod(
                                        startDate = LocalDate.of(2026, 4, 8),
                                        endDate = LocalDate.of(2026, 4, 9),
                                    ),
                            ),
                        room =
                            ScreeningRoom(
                                name = ScreeningRoomName("커피"),
                                operatingTime = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
                                seats =
                                    Seats(
                                        listOf(
                                            Seat(
                                                position =
                                                    SeatPosition(
                                                        Row("A"),
                                                        Column(1),
                                                    ),
                                            ),
                                        ),
                                    ),
                            ),
                        startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
                    ),
                ),
        )
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

            ticketBucket.addTicket(Ticket(screening, selectedSeats))
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
