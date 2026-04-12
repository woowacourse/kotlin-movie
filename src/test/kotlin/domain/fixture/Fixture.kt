package domain.fixture

import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.ScreeningPeriod
import domain.movie.Title
import domain.screening.Screening
import domain.screening.ScreeningRoom
import domain.screening.ScreeningRoomName
import domain.screening.TimeRange
import domain.seat.Column
import domain.seat.Row
import domain.seat.Seat
import domain.seat.SeatPosition
import domain.seat.SeatPositions
import domain.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun createSeatPosition(
    row: String = "A",
    column: Int = 1,
) = SeatPosition(Row.valueOf(row.uppercase()), Column(column))

fun createSeatPositions(vararg positions: Pair<String, Int> = arrayOf("A" to 1)): SeatPositions =
    SeatPositions(positions.map { (row, col) -> createSeatPosition(row, col) })

fun createSeats(vararg positions: Pair<String, Int> = arrayOf("A" to 1)): Seats =
    Seats(positions.map { (row, col) -> Seat(position = createSeatPosition(row, col)) })

fun createMovie(
    title: String = "영화 1",
    runningTime: RunningTime = RunningTime(160),
    startDate: LocalDate = LocalDate.of(2026, 4, 10),
    endDate: LocalDate = LocalDate.of(2026, 4, 11),
) = Movie(
    title = Title(title),
    runningTime = runningTime,
    screeningPeriod = ScreeningPeriod(startDate, endDate),
)

fun createScreeningRoom(
    name: String = "상영관 1",
    openAt: LocalTime = LocalTime.of(10, 0),
    closeAt: LocalTime = LocalTime.of(18, 0),
    seats: Seats = createSeats(positions = createScreeningRoomSeats()),
) = ScreeningRoom(
    name = ScreeningRoomName(name),
    operatingTime = TimeRange(openAt, closeAt),
    seats = seats,
)

fun createScreeningRoomSeats(): Array<Pair<String, Int>> =
    ('A'..'E')
        .flatMap { row ->
            (1..4).map { col -> "$row" to col }
        }.toTypedArray()

fun createScreening(
    movie: Movie = createMovie(),
    room: ScreeningRoom = createScreeningRoom(),
    startTime: LocalDateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
) = Screening(movie = movie, room = room, startTime = startTime)
