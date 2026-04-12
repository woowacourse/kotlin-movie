package model

import model.movie.Movie
import model.movie.Movies
import model.schedule.Schedule
import model.schedule.Screening
import model.seat.SeatInventory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object MockData {
    val TOP_GUN_MAVERICK =
        Movie(
            title = "탑건: 매버릭",
            runningTime = 130,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val MATHER =
        Movie(
            title = "마더",
            runningTime = 100,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val THE_LAST_10_YEARS =
        Movie(
            title = "남은 인생 10년",
            runningTime = 120,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val SPIDER_MAN_NO_WAY_HOME =
        Movie(
            title = "스파이더맨: 노 웨이 홈",
            runningTime = 140,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val IRON_MAN_3 =
        Movie(
            title = "아이언맨 3",
            runningTime = 120,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val EVEN_IF_THIS_LOVE_DISAPPEARS_TO_THE_WORLD_TONIGHT =
        Movie(
            title = "오늘 밤 이세상에서 사랑이 사라진다 해도",
            runningTime = 100,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val CHAIN_SO_MAN =
        Movie(
            title = "체인소맨",
            runningTime = 100,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val HOPPERS =
        Movie(
            title = "호퍼스",
            runningTime = 100,
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30),
        )

    val movies =
        Movies(
            value =
                listOf(
                    TOP_GUN_MAVERICK,
                    MATHER,
                    SPIDER_MAN_NO_WAY_HOME,
                    IRON_MAN_3,
                    EVEN_IF_THIS_LOVE_DISAPPEARS_TO_THE_WORLD_TONIGHT,
                    CHAIN_SO_MAN,
                    HOPPERS,
                    THE_LAST_10_YEARS,
                ),
        )

    // 9시에 오픈하고 23시 59분에 종료되는 상영관
    val mockSchedule =
        Schedule(
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(23, 59),
            // 2026년 4월 10일의 영화 상영 시간표
            screenings =
                listOf(
                    Screening(
                        movie = TOP_GUN_MAVERICK,
                        startDateTime = LocalDateTime.of(2026, 4, 10, 10, 0),
                        seatInventory = SeatInventory.createDefaultSeatInventory(),
                    ),
                    Screening(
                        movie = THE_LAST_10_YEARS,
                        startDateTime = LocalDateTime.of(2026, 4, 10, 13, 0),
                        seatInventory = SeatInventory.createDefaultSeatInventory(),
                    ),
                    Screening(
                        movie = EVEN_IF_THIS_LOVE_DISAPPEARS_TO_THE_WORLD_TONIGHT,
                        startDateTime = LocalDateTime.of(2026, 4, 10, 16, 0),
                        seatInventory = SeatInventory.createDefaultSeatInventory(),
                    ),
                    Screening(
                        movie = TOP_GUN_MAVERICK,
                        startDateTime = LocalDateTime.of(2026, 4, 10, 18, 0),
                        seatInventory = SeatInventory.createDefaultSeatInventory(),
                    ),
                    Screening(
                        movie = HOPPERS,
                        startDateTime = LocalDateTime.of(2026, 4, 10, 21, 0),
                        seatInventory = SeatInventory.createDefaultSeatInventory(),
                    ),
                    Screening(
                        movie = THE_LAST_10_YEARS,
                        startDateTime = LocalDateTime.of(2026, 4, 10, 9, 30),
                        seatInventory = SeatInventory.createDefaultSeatInventory(),
                    ),
                ),
        )
}
