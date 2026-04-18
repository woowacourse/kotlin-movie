package persistence

import domain.reservation.ReservationInfo
import domain.seat.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class CinemaDatabaseTest {
    @Test
    fun `기본 상영 데이터를 저장한 뒤 다시 조회할 수 있다`() {
        val database = CinemaDatabase.inMemory(UUID.randomUUID().toString())

        val movieTheater = database.loadMovieTheater()

        assertThat(movieTheater.movies.map { it.id.value }).isEqualTo(listOf("movie-f1", "movie-iron-man", "movie-toy-story"))
        assertThat(movieTheater.movies.map { it.title }).isEqualTo(listOf("F1 더 무비", "아이언맨", "토이 스토리"))
        assertThat(movieTheater.screens.map { it.id.value }.sorted()).isEqualTo(listOf("screen-1", "screen-2", "screen-3"))
        assertThat(movieTheater.screenings.map { "${it.movie.id.value}/${it.screen.id.value}/${it.startTime}" }.sorted())
            .isEqualTo(
                listOf(
                    "movie-f1/screen-1/2025-09-20T10:20",
                    "movie-f1/screen-1/2025-09-20T13:00",
                    "movie-f1/screen-1/2025-09-20T15:40",
                    "movie-f1/screen-1/2025-09-20T20:10",
                    "movie-iron-man/screen-3/2025-09-20T09:50",
                    "movie-toy-story/screen-2/2025-09-20T13:30",
                    "movie-toy-story/screen-2/2025-09-20T16:00",
                ),
            )
    }

    @Test
    fun `예매 정보를 저장한 뒤 다시 조회할 수 있다`() {
        val database = CinemaDatabase.inMemory(UUID.randomUUID().toString())
        val movieTheater = database.loadMovieTheater()
        val firstScreening = movieTheater.screenings.first()
        val secondScreening = movieTheater.screenings.last()
        val reservations =
            listOf(
                ReservationInfo(firstScreening, firstScreening.screen.findSeat('C', 1)!!),
                ReservationInfo(secondScreening, secondScreening.screen.findSeat('E', 4)!!),
            )

        database.saveReservations(reservations)

        val actual = database.findReservations()
        val expected =
            reservations
                .sortedBy { it.screening.startTime.toString() }
                .map {
                    "${it.screening.movie.id.value}/${it.screening.screen.id.value}/${it.screening.startTime}/${it.seat.coordinate.row}${it.seat.coordinate.column}"
                }

        assertThat(actual).hasSize(2)
        assertThat(
            actual.map {
                "${it.screening.movie.id.value}/${it.screening.screen.id.value}/${it.screening.startTime}/${it.seat.coordinate.row}${it.seat.coordinate.column}"
            },
        ).isEqualTo(expected)
        assertThat(actual).allSatisfy { assertThat(it.seat.isReserved).isEqualTo(SeatState.RESERVED) }
    }

    @Test
    fun `저장된 예매 정보가 좌석 예약 상태에 반영된다`() {
        val database = CinemaDatabase.inMemory(UUID.randomUUID().toString())
        val movieTheater = database.loadMovieTheater()
        val screening = movieTheater.screenings.first()
        val reservedSeat = screening.screen.findSeat('C', 1)!!

        database.saveReservations(listOf(ReservationInfo(screening, reservedSeat)))

        val reloadedMovieTheater = database.loadMovieTheater()
        val reloadedScreening =
            reloadedMovieTheater.screenings.first {
                it.movie.id.value == screening.movie.id.value &&
                    it.screen.id.value == screening.screen.id.value &&
                    it.startTime == screening.startTime
            }

        assertThat(reloadedScreening.screen.findSeat('C', 1)!!.isReserved).isEqualTo(SeatState.RESERVED)
        assertThat(reloadedScreening.screen.findSeat('C', 2)!!.isReserved).isEqualTo(SeatState.AVAILABLE)
    }

    @Test
    fun `테스트 간 데이터가 서로 영향을 주지 않는다`() {
        val firstDatabase = CinemaDatabase.inMemory(UUID.randomUUID().toString())
        val secondDatabase = CinemaDatabase.inMemory(UUID.randomUUID().toString())
        val firstScreening = firstDatabase.loadMovieTheater().screenings.first()
        val reservedSeat = firstScreening.screen.findSeat('C', 1)!!

        firstDatabase.saveReservations(listOf(ReservationInfo(firstScreening, reservedSeat)))

        val secondMovieTheater = secondDatabase.loadMovieTheater()
        val secondScreening = secondMovieTheater.screenings.first()

        assertThat(firstDatabase.findReservations()).hasSize(1)
        assertThat(secondDatabase.findReservations()).isEmpty()
        assertThat(secondScreening.screen.findSeat('C', 1)!!.isReserved).isEqualTo(SeatState.AVAILABLE)
    }
}
