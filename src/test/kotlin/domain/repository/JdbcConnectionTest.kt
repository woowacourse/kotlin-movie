package domain.repository

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import repository.JdbcConnection

class JdbcConnectionTest {

    @Test
    fun `H2 데이터베이스 연결을 가져올 수 있다`() {
        val connection = JdbcConnection.getConnection()

        connection shouldNotBe null
        connection.isClosed shouldBe false
    }

    @Test
    fun `연결된 커넥션은 유효해야 한다`() {
        val connection = JdbcConnection.getConnection()

        connection.isValid(1).shouldBeTrue()
        
        connection.close()
    }

    @Test
    fun `설정된 DB URL이 올바른지 확인한다`() {
        val connection = JdbcConnection.getConnection()
        
        connection.metaData.url shouldBe "jdbc:h2:./test"
        
        connection.close()
    }
}
