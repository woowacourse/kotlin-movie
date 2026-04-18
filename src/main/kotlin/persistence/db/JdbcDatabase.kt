package persistence.db

import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager

internal class JdbcDatabase(
    private val config: DatabaseConfig,
) {
    fun <T> withConnection(block: (Connection) -> T): T =
        DriverManager.getConnection(config.jdbcUrl, config.username, config.password).use(block)

    fun <T> withTransaction(block: (Connection) -> T): T =
        withConnection { connection ->
            connection.autoCommit = false
            runCatching {
                block(connection).also { connection.commit() }
            }.getOrElse { exception ->
                connection.rollback()
                throw exception
            }
        }

    companion object {
        fun file(databasePath: Path): JdbcDatabase = JdbcDatabase(DatabaseConfig("jdbc:h2:file:${databasePath.toAbsolutePath()}"))

        fun inMemory(name: String): JdbcDatabase = JdbcDatabase(DatabaseConfig("jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1"))
    }
}
