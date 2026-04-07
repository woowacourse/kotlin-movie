import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MovieTest {
    @Test
    fun `러닝 타임이 같아도 id가 다르면 다른 영화로 판단한다`() {
        assertThat(
            Movie(
                id = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                runningTime = RunningTime(10),
            ),
        ).isNotEqualTo(
            Movie(
                id = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                runningTime = RunningTime(10),
            ),
        )
    }
}
