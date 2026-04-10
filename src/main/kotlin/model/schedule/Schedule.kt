package model.schedule

import model.movie.Movie
import model.seat.SeatInventory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Schedule(
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val screenings: List<Screening>,
) {
    init {
        require(openTime < closeTime) { "시작 시간이 종료 시간보다 클 수 없습니다" }
    }

    // 변경된 Screening에 대한 Schedule 변경 함수
    fun updateScreening(
        old: Screening,
        new: Screening,
    ): Schedule =
        Schedule(
            openTime = openTime,
            closeTime = closeTime,
            screenings = screenings.map {
                if (it == old) new else it
            },
        )

    // 제목과 일치하는 영화의 상영 목록을 반환하는 함수
    fun getScreeningsByMovie(movie: Movie): List<Screening> =
        screenings.filter { it.movie == movie }

    companion object {
        // 2026년 4월 10일의 영화 상영 시간표
        val screenings =
            listOf(
                Screening(
                    movie =
                        Movie(
                            movieTitle = "탑건: 매버릭",
                            movieRunningTime = 130,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                    startDateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
                    seatInventory = SeatInventory.createDefaultSeatInventory(),
                ),
                Screening(
                    movie =
                        Movie(
                            movieTitle = "남은 인생 10년",
                            movieRunningTime = 130,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                    startDateTime = LocalDateTime.of(2026, 4, 10, 13, 0),
                    seatInventory = SeatInventory.createDefaultSeatInventory(),
                ),
                Screening(
                    movie =
                        Movie(
                            movieTitle = "오늘 밤 이세상에서 사랑이 사라진다 해도",
                            movieRunningTime = 130,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                    startDateTime = LocalDateTime.of(2026, 4, 10, 16, 0),
                    seatInventory = SeatInventory.createDefaultSeatInventory(),
                ),
                Screening(
                    movie =
                        Movie(
                            movieTitle = "탑건: 매버릭",
                            movieRunningTime = 130,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                    startDateTime = LocalDateTime.of(2026, 4, 10, 18, 0),
                    seatInventory = SeatInventory.createDefaultSeatInventory(),
                ),
                Screening(
                    movie =
                        Movie(
                            movieTitle = "호퍼스",
                            movieRunningTime = 130,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                    startDateTime = LocalDateTime.of(2026, 4, 10, 21, 0),
                    seatInventory = SeatInventory.createDefaultSeatInventory(),
                ),
            )
    }
}
