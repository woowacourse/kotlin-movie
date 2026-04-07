import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class RunningTimeTest {
    @Test
    fun `러닝 타임이 0 이하일 경우 예외를 던진다`() {
        assertThatThrownBy {
            RunningTime(-1)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `러닝 타임이 양수라면 해당하는 값이 저장된다`() {
        val runningTime = RunningTime(100)
        assertThat(runningTime.minute).isEqualTo(100)
    }

    @Test
    fun `러닝 타임의 값이 서로 같다면 동등하다`() {
        assertThat(RunningTime(100)).isEqualTo(RunningTime(100))
    }
}
