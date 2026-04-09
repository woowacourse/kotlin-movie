package controller

import domain.ScreeningSchedule
import domain.ScreeningTemplate
import domain.Seat
import domain.model.Movie
import domain.model.screen.Screening
import domain.model.seat.SeatAvailability
import java.time.LocalDate
import java.time.LocalTime

// 영화관 전체 도메인 조합 객체
class CinemaController(
    // 스케줄 상영 기간 (예: 4/6 ~ 4/13)
    private val screeningPeriodStart: LocalDate = LocalDate.of(2026, 4, 6),
    private val screeningPeriodEnd: LocalDate = LocalDate.of(2026, 4, 13),
    private val screeningSchedule: ScreeningSchedule =
        ScreeningSchedule.withSamples(
            screeningPeriodStart = screeningPeriodStart,
            screeningPeriodEnd = screeningPeriodEnd,
            movies = Movie.sampleMovies,
            samples = defaultScreeningSeeds(),
        ),
) {
    // 상영 기간 내 특정 날짜/시간에 새 상영을 등록하고, 등록된 상영 객체를 반환한다.
    fun createScreening(
        movieTitle: String,
        screeningDate: LocalDate,
        startTime: LocalTime,
    ): Screening = screeningSchedule.createScreening(movieTitle, screeningDate, startTime)

    // 특정 제목의 영화 상영 목록을 조회한다.
    fun findScreeningTitle(title: String): List<Screening> = screeningSchedule.screeningsOfMovieTitle(title)

    // 특정 날짜의 특정 영화 상영 목록을 조회한다.
    fun findScreeningsDate(
        screening: List<Screening>,
        date: LocalDate,
    ): List<Screening> = screeningSchedule.screeningsOfMovieDate(screening, date)

    // 특정 상영의 좌석 상태를 조회한다.
    fun findSeatStatuses(
        movieTitle: String,
        date: LocalDate,
        startTime: LocalTime,
    ): List<SeatAvailability> = screeningSchedule.seatStatusesOf(movieTitle, date, startTime)

    // 특정 상영에 대해 선택 좌석들을 예약 처리하고, 예약이 반영된 상영을 반환한다.
    fun reserveSeats(
        movieTitle: String,
        date: LocalDate,
        startTime: LocalTime,
        seats: List<Seat>,
    ): Screening = screeningSchedule.reserveSeats(movieTitle, date, startTime, seats)
}

private fun defaultScreeningSeeds(): List<ScreeningTemplate> =
    // 상영 제목 , 상영 날짜, 상영 시작 시간 리스트들
    listOf(
        ScreeningTemplate(
            movieTitle = "탑건: 매버릭",
            screeningDate = LocalDate.of(2026, 4, 6),
            startTime = LocalTime.of(10, 0),
        ),
        ScreeningTemplate(
            movieTitle = "마더",
            screeningDate = LocalDate.of(2026, 4, 6),
            startTime = LocalTime.of(13, 0),
        ),
        ScreeningTemplate(
            movieTitle = "아이언맨 3",
            screeningDate = LocalDate.of(2026, 4, 6),
            startTime = LocalTime.of(16, 0),
        ),
        ScreeningTemplate(
            movieTitle = "탑건: 매버릭",
            screeningDate = LocalDate.of(2026, 4, 7),
            startTime = LocalTime.of(10, 0),
        ),
        ScreeningTemplate(
            movieTitle = "스파이더맨: 노 웨이 홈",
            screeningDate = LocalDate.of(2026, 4, 7),
            startTime = LocalTime.of(13, 30),
        ),
        ScreeningTemplate(
            movieTitle = "탑건: 매버릭",
            screeningDate = LocalDate.of(2026, 4, 8),
            startTime = LocalTime.of(10, 0),
        ),
        ScreeningTemplate(
            movieTitle = "남은 인생 10년",
            screeningDate = LocalDate.of(2026, 4, 8),
            startTime = LocalTime.of(14, 0),
        ),
        ScreeningTemplate(
            movieTitle = "오늘 밤 이세상에서 사랑이 사라진다 해도",
            screeningDate = LocalDate.of(2026, 4, 9),
            startTime = LocalTime.of(12, 20),
        ),
        ScreeningTemplate(
            movieTitle = "아이언맨 3",
            screeningDate = LocalDate.of(2026, 4, 10),
            startTime = LocalTime.of(9, 50),
        ),
        ScreeningTemplate(
            movieTitle = "체인소맨",
            screeningDate = LocalDate.of(2026, 4, 10),
            startTime = LocalTime.of(16, 0),
        ),
        ScreeningTemplate(
            movieTitle = "호퍼스",
            screeningDate = LocalDate.of(2026, 4, 11),
            startTime = LocalTime.of(20, 10),
        ),
        ScreeningTemplate(
            movieTitle = "스파이더맨: 노 웨이 홈",
            screeningDate = LocalDate.of(2026, 4, 12),
            startTime = LocalTime.of(15, 40),
        ),
        ScreeningTemplate(
            movieTitle = "마더",
            screeningDate = LocalDate.of(2026, 4, 13),
            startTime = LocalTime.of(11, 0),
        ),
    )
