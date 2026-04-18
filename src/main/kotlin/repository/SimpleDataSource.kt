package repository

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.PrintWriter
import java.sql.Connection
import java.util.logging.Logger
import javax.sql.DataSource

@Configuration
class DataSourceConfig {
    @Bean
    fun dataSource(): DataSource = SimpleDataSource()

    @Bean
    fun databaseInitializer(dataSource: DataSource): ApplicationRunner = ApplicationRunner {
        dataSource.connection.use { connection ->
            val schema = ClassPathResource("schema.sql").inputStream.bufferedReader().use { it.readText() }
            connection.createStatement().use { stmt ->
                stmt.execute(schema)
            }
        }
    }
}

class SimpleDataSource : DataSource {
    override fun getConnection(): Connection = JdbcConnection.getConnection()
    override fun getConnection(username: String?, password: String?): Connection = JdbcConnection.getConnection()
    override fun getLogWriter(): PrintWriter? = null
    override fun setLogWriter(out: PrintWriter?) {}
    override fun setLoginTimeout(seconds: Int) {}
    override fun getLoginTimeout(): Int = 0
    override fun <T : Any?> unwrap(iface: Class<T>?): T { throw UnsupportedOperationException() }
    override fun isWrapperFor(iface: Class<*>?): Boolean = false
    override fun getParentLogger(): Logger { throw UnsupportedOperationException() }
}
