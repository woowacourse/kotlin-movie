@file:Suppress("NonAsciiCharacters")

package movie.database

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DatabaseConnectionFactoryTest {
    @Test
    fun `설정된 JDBC URL을 통해 In-Memory 데이터베이스에 정상적으로 연결된다`() {
        // given
        val isLocal = false

        // when
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        // then
        Assertions.assertThat(connection).isNotNull
        Assertions.assertThat(connection.isClosed).isFalse
        connection.close()
    }
}
