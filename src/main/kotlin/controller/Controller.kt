package controller

import domain.discountpolicy.PayMethod
import domain.paycalculator.PayCalculator
import domain.reservations.Reservations
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.timetable.MockTimeTable
import domain.timetable.TimeTable
import domain.timetable.items.Screen
import domain.timetable.items.ScreeningSchedule
import view.input.InputView
import view.output.OutputView
import java.time.LocalDate

class Controller(
    val inputView: InputView,
    val outputView: OutputView,
    val timeTable: TimeTable = TimeTable(MockTimeTable.timeTable),
) {
    fun run() {
        if (!startReserve()) return

        val reservations = makeReserve()

        payProcessor(reservations)
    }

    fun startReserve(): Boolean {
        try {
            return inputView.readStartReserve()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return startReserve()
        }
    }

    fun makeReserve(): Reservations {
        val reservations = Reservations()

        val titleSearchResult = searchMovieWithTitle()
        val dateSearchResult = searchMovieWithDate(titleSearchResult)
        val selectedSchedule = selectMovieSchedule(dateSearchResult)
        val selectedSeats = selectSeats(selectedSchedule)
        reservations.addReservation(
            addReservation(
                screeningSchedule = selectedSchedule,
                selectedSeats = selectedSeats,
            ),
        )

        if (!inputView.readContinue()) return reservations
        return makeReserve()
    }

    fun searchMovieWithTitle(): TimeTable {
        try {
            val title = inputView.readMovieTitle()
            return timeTable.getMovieSchedulesWithTitle(title)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return searchMovieWithTitle()
        }
    }

    fun searchMovieWithDate(timeTable: TimeTable): TimeTable {
        try {
            val date = inputView.readDate()
            val localDate = LocalDate.of(date[0], date[1], date[2])
            return timeTable.getMovieSchedulesWithDate(localDate)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return searchMovieWithDate(timeTable)
        }
    }

    fun selectMovieSchedule(timeTable: TimeTable): ScreeningSchedule {
        outputView.printScreeningMovieTime(timeTable.getSchedules())
        try {
            val index = inputView.readScreeningNumber(timeTable.countSchedule())
            return timeTable.getScheduleWithIndex(index - 1)
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return selectMovieSchedule(timeTable)
        }
    }

    fun selectSeats(screeningSchedule: ScreeningSchedule): List<Seat> {
        outputView.printSeatMap(Screen.seatMap)
        try {
            val seatNumbers = inputView.readSeatNumber()
            seatNumbers.forEach {
                if (screeningSchedule.isReservedSeat(it)) {
                    throw IllegalArgumentException("해당 좌석은 이미 예매되어 있습니다.")
                }
            }
            val seats = mutableListOf<Seat>()
            seatNumbers.forEach {
                seats.add(Seat(it))
            }
            return seats.toList()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return selectSeats(screeningSchedule)
        }
    }

    fun addReservation(
        screeningSchedule: ScreeningSchedule,
        selectedSeats: List<Seat>,
    ): Reservation {
        val movie = screeningSchedule.getMovie()
        val screenTime = screeningSchedule.getScreenTime()

        val newReservation = Reservation(
            movie = movie,
            screenTime = screenTime,
            seats = selectedSeats,
        )
        outputView.printAddReservation(newReservation)
        return newReservation
    }

    fun payProcessor(reservations: Reservations) {
        val reservationItems = reservations.reservations
        outputView.printFinalReservations(reservationItems)
        val payCalculator = PayCalculator(reservationItems)

        val usePoint = getUsePoint()
        val payMethod = getUsePayMethod()

        payCalculator.usePoint(usePoint)
        payCalculator.pay(payMethod)
        outputView.printFinalPrice(payCalculator.getTotalPrice().getAmount())

        if (!inputView.readPayAgreement()) return
        outputView.printReceipt(
            reservations.reservations,
            payCalculator.getTotalPrice().getAmount(),
        )
    }

    fun getUsePoint(): Int {
        try {
            return inputView.readUsePoint()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return getUsePoint()
        }
    }

    fun getUsePayMethod(): PayMethod {
        try {
            return inputView.readPayMethod()
        } catch (e: IllegalArgumentException) {
            println(e.message)
            return getUsePayMethod()
        }
    }
}
