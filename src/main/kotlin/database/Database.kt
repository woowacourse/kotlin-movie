package database

import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val DEFAULT_URL = "jdbc:h2:./data/cinema;DB_CLOSE_DELAY=-1"
    private var url: String = DEFAULT_URL

    fun init(url: String = DEFAULT_URL) {
        this.url = url
        val schema = loadSchema()
        connection().use { connection ->
            connection.createStatement().use { it.execute(schema) }
        }
        DataInitializer.initialize()
    }

    fun connection(): Connection = DriverManager.getConnection(url, "sa", "")

    private fun loadSchema(): String {
        val stream =
            Database::class.java.classLoader.getResourceAsStream("schema.sql")
                ?: error("schema.sql 이 없습니다.")
        return stream.bufferedReader().use { it.readText() }
    }
}
