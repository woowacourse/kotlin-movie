package domain

import domain.common.Money
import domain.common.TimeRange
import domain.discount.MoviedayDiscount
import domain.discount.PaymentDiscount
import domain.discount.TicketDiscountPolicy
import domain.discount.TimeDiscount
import domain.discount.TotalDiscountPolicy
import domain.movie.*
import domain.payment.PaymentType
import domain.reservation.Reservation
import domain.screening.*
import domain.seat.*
import domain.ticket.Ticket
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object DomainTestFixture {
    fun createMovie(
        id: Long? = 1L,
        title: String = "허닛",
        runningTime: Int = 167,
        startDate: LocalDate = LocalDate.of(2026, 4, 8)
    ) = Movie(
        id = id,
        title = Title(title),
        runningTime = RunningTime(runningTime),
        screeningPeriod = ScreeningPeriod(
            startDate = startDate,
            endDate = startDate.plusDays(1)
        )
    )

    fun createScreeningRoom(
        id: Long? = 1L,
        name: String = "커피",
        operatingTime: TimeRange = TimeRange(LocalTime.of(10, 0), LocalTime.of(18, 0)),
        seats: List<Seat> = listOf(Seat(position = SeatPosition(Row("A"), Column(1))))
    ) = ScreeningRoom(
        id = id,
        name = ScreeningRoomName(name),
        operatingTime = operatingTime,
        seats = Seats(seats)
    )

    fun createScreening(
        id: Long? = 1L,
        movie: Movie = createMovie(),
        room: ScreeningRoom = createScreeningRoom(),
        startTime: LocalDateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
    ) = Screening(
        id = id,
        movie = movie,
        room = room,
        startTime = startTime,
    )

    fun createTicket(
        screening: Screening = createScreening(),
        seatPositions: SeatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1))))
    ) = Ticket(screening, seatPositions)

    val ticketDiscountPolicy = TicketDiscountPolicy(
        strategies = listOf(
            MoviedayDiscount(),
            TimeDiscount()
        )
    )

    val totalDiscountPolicy = TotalDiscountPolicy(
        strategies = listOf(PaymentDiscount())
    )

    fun seatA1() = SeatPosition(Row("A"), Column(1))
    fun seatA2() = SeatPosition(Row("A"), Column(2))

    fun createReservation(
        id: Long? = null,
        tickets: List<Ticket> = listOf(createTicket()),
        usedPoints: Money = Money(1_000),
        paymentMethod: PaymentType = PaymentType.CREDIT_CARD,
        totalPrice: Money = Money(20_000)
    ) = Reservation(
        id = id,
        tickets = tickets,
        usedPoints = usedPoints,
        paymentMethod = paymentMethod,
        totalPrice = totalPrice
    )

    fun createTicketWithSeats(vararg positions: SeatPosition) =
        Ticket(createScreening(), SeatPositions(positions.toList()))
}
