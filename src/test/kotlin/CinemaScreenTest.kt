import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CinemaScreenTest {
    @Test
    fun `운영 기간이 같더라도 id가 다르면 다른 상영관으로 판단한다`() {
        val dateTimeRange =
            DateTimeRange(
                LocalDateTime.of(2026, 4, 7, 21, 50),
                LocalDateTime.of(2026, 4, 7, 22, 50),
            )
        assertThat(
            CinemaScreen(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                dateTimeRange = dateTimeRange,
            ),
        ).isNotEqualTo(
            CinemaScreen(
                id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                dateTimeRange = dateTimeRange,
            ),
        )
    }

    @Test
    fun `운영 기간이 다르더라도 id가 같으면 같은 상영관으로 판단한다`() {
        val dateTimeRange =

            assertThat(
                CinemaScreen(
                    id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                    dateTimeRange =
                        DateTimeRange(
                            LocalDateTime.of(2026, 4, 7, 21, 50),
                            LocalDateTime.of(2026, 4, 7, 22, 50),
                        ),
                ),
            ).isNotEqualTo(
                CinemaScreen(
                    id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                    dateTimeRange =
                        DateTimeRange(
                            LocalDateTime.of(1999, 4, 7, 21, 50),
                            LocalDateTime.of(2026, 4, 7, 22, 50),
                        ),
                ),
            )
    }
}
