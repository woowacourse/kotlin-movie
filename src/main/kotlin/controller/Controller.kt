package controller

import domain.ScreeningSchedule
import domain.model.Column
import domain.model.Money
import domain.model.Movie
import domain.model.PaymentSystem
import domain.model.PaymentType
import domain.model.Point
import domain.model.Row
import domain.model.RunningTime
import domain.model.Screening
import domain.model.ScreeningPeriod
import domain.model.ScreeningRoom
import domain.model.ScreeningRoomName
import domain.model.Seat
import domain.model.SeatPosition
import domain.model.SeatPositions
import domain.model.Seats
import domain.model.Ticket
import domain.model.TicketBucket
import domain.model.TimeRange
import domain.model.Title
import view.InputView
import view.OutputView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Controller(
) {
    val schedule = ScreeningSchedule(
        screenings = listOf(
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
                        seats = Seats(
                            listOf(
                                Seat(
                                    position = SeatPosition(
                                        Row("A"),
                                        Column(1)
                                    )
                                )
                            )
                        ),
                    ),
                startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
            )
        )
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

        OutputView.displayResult(ticketBucket,totalPrice,point)

    }


    private fun startReservation(): Boolean {
        return retryUntilValid {
            OutputView.createNewReservePrompt()
            val input = InputView.read()
            InputParser.parseYN(input)
        }
    }

    private fun getMovie(): Title {
        return retryUntilValid {
            OutputView.reserveMoviePrompt()
            val input = InputView.read()
            InputParser.parseMovieTitle(input)
        }
    }

    private fun getReserveDate(): LocalDate {
        return retryUntilValid {
            OutputView.reserveDatePrompt()
            val input = InputView.read()
            InputParser.parseDate(input)
        }
    }

    private fun selectMovieScreening(movieSchedule: ScreeningSchedule): Screening {
        return retryUntilValid {
            OutputView.selectMovieSchedulePrompt(movieSchedule)
            val input = InputView.read()
            val select = InputParser.parseNum(input)
            movieSchedule.screenings[select - 1]
        }
    }

    private fun selectSeats(ticketBucket: TicketBucket, screening: Screening): TicketBucket {
        return retryUntilValid {
            OutputView.selectSeatsPrompt(screening.seats)
            val input = InputView.read()
            val selectedSeats = InputParser.parseSeats(input)
            screening.canReserve(selectedSeats)

            ticketBucket.addTicket(Ticket(screening, selectedSeats))
        }
    }

    private fun confirmAddExtraMovie(): Boolean {
        return retryUntilValid {
            OutputView.addExtraMoviePrompt()
            val input = InputView.read()
            InputParser.parseYN(input)
        }
    }

    private fun getPoint(): Point {
        return retryUntilValid {
            OutputView.pointPrompt()
            val input = InputView.read()
            InputParser.parsePoint(input)
        }
    }

    private fun getPaymentType(): PaymentType {
        return retryUntilValid {
            OutputView.selectPaymentType()
            val input = InputView.read()
            InputParser.parsePaymentType(input)
        }
    }

    private fun confirmPurchase(totalPrice: Money): Boolean {
        return retryUntilValid {
            OutputView.decideToPayPrompt(totalPrice)
            val input = InputView.read()
            InputParser.parseYN(input)
        }
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
