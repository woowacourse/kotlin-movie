package domain.fixture

import domain.Column
import domain.Movie
import domain.Row
import domain.RunningTime
import domain.Screening
import domain.ScreeningPeriod
import domain.ScreeningRoom
import domain.ScreeningRoomName
import domain.Seat
import domain.SeatPosition
import domain.SeatPositions
import domain.Seats
import domain.TimeRange
import domain.Title
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun createSeatPosition(
    row: String = "A",
    column: Int = 1,
) = SeatPosition(Row(row), Column(column))

fun createSeatPositions(vararg positions: Pair<String, Int> = arrayOf("A" to 1)): SeatPositions =
    SeatPositions(positions.map { (row, col) -> createSeatPosition(row, col) })

fun createSeats(vararg positions: Pair<String, Int> = arrayOf("A" to 1)): Seats =
    Seats(positions.map { (row, col) -> Seat(position = createSeatPosition(row, col)) })

fun createMovie(
    title: String = "영화 1",
    startDate: LocalDate = LocalDate.of(2026, 4, 10),
    endDate: LocalDate = LocalDate.of(2026, 4, 11),
) = Movie(
    title = Title(title),
    runningTime = RunningTime(160),
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
