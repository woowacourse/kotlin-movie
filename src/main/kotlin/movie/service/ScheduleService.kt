package movie.service

import movie.domain.Movie
import movie.domain.MovieTitle
import movie.domain.Schedule
import movie.domain.Schedules
import movie.domain.seat.SeatNumber
import movie.domain.seat.SelectedSeats
import movie.dto.MovieScheduleDto
import movie.repository.ScheduleRepository

class ScheduleService(
    private val scheduleRepository: ScheduleRepository
) {
    fun getSchedules(): Schedules {
        val movieSchedules = scheduleRepository.findAllSchedule()
        return mapToDomain(movieSchedules)
    }

    private fun mapToDomain(movieSchedules: List<MovieScheduleDto>): Schedules {
        val scheduleList = movieSchedules.map { dto ->
            val seatNumbers = dto.reservedSeats.map { SeatNumber(it.seatNumber) }
            val selectedSeats = SelectedSeats(seatNumbers)

            Schedule(
                id = dto.scheduleId,
                movie = Movie(MovieTitle(dto.title), dto.runningTime),
                startTime = dto.startTime,
                endTime = dto.endTime,
                selectedSeat = selectedSeats
            )
        }
        return Schedules(scheduleList)
    }
}
