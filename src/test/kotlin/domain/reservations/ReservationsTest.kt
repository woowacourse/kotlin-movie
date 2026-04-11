package domain.reservations

import domain.movie.Movie
import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import domain.reservations.items.Reservation
import domain.seat.Seat
import domain.seat.items.GradeA
import domain.seat.items.GradeB
import domain.seat.items.GradeS
import domain.seat.items.SeatGrade
import domain.seat.items.SeatPosition
import domain.timetable.items.ScreenTime
import domain.timetable.items.Seats
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ReservationsTest {
    @Test
    fun `reservations 목록 중 입력받은 reservation과 겹치는 reservation이 없으면 추가한다`() {
        val reservations = Reservations()
    }

    @Test
    fun `reservations 목록 중 입력받은 reservation과 겹치는 reservation이 있으면 에러를 발생한다`() {
    }

    private fun createReservation(screenTime: ScreenTime) =
        Reservation(
            movie =
                Movie(
                    title = Title("신바드의 모험"),
                    runningTime = RunningTime(120),
                    screeningPeriod =
                        ScreeningPeriod(
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                ),
            screenTime = screenTime,
            seats =
                Seats(
                    seats =
                        listOf(
                            createSeat(GradeB()),
                            createSeat(GradeA()),
                            createSeat(GradeS()),
                        ),
                ),
        )

    private fun createSeat(grade: SeatGrade) =
        Seat(
            seatPosition = SeatPosition.of("A1"),
            seatGrade = grade,
        )
}
