package domain

import domain.fixture.createMovie
import domain.fixture.createScreening
import domain.fixture.createSeatPositions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class TicketBucketTest {
    @Test
    fun `티켓들을 가진다`() {
        TicketBucket(
            tickets =
                listOf(
                    Ticket(
                        screening = createScreening(),
                        seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
                    ),
                ),
        )
    }

    @Test
    fun `티켓을 추가할 때, 상영시간이 겹치면 예외를 던진다`() {
        val bucket =
            TicketBucket(
                tickets =
                    listOf(
                        Ticket(
                            screening = createScreening(movie = createMovie(title = "허닛")),
                            seatPositions = SeatPositions(listOf(SeatPosition(Row("A"), Column(1)))),
                        ),
                    ),
            )

        assertThrows(IllegalArgumentException::class.java) {
            bucket.addTicket(
                screening = createScreening(movie = createMovie(title = "Cㅓ비")),
                positions = createSeatPositions(),
            )
        }
    }

    @Test
    fun `티켓을 추가하면 티켓이 추가된 장바구니를 반환한다`() {
        val screening = createScreening()
        val bucket =
            TicketBucket(
                tickets =
                    listOf(
                        Ticket(
                            screening = screening,
                            seatPositions = createSeatPositions(),
                        ),
                    ),
            )

        val result =
            bucket.addTicket(
                screening = screening,
                positions = createSeatPositions("B" to 1),
            )
        assertThat(result.tickets.size).isEqualTo(2)
    }
}
