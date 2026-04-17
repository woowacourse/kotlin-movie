package db

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConfig {
    private var url = "jdbc:h2:~/kotlin-movie"
    private const val USER = "sa"
    private const val PASSWORD = ""

    fun configure(newUrl: String) {
        url = newUrl
    }

    fun getConnection(): Connection = DriverManager.getConnection(url, USER, PASSWORD)

    fun initialize() {
        val ddl =
            DatabaseConfig::class.java
                .getResourceAsStream("/schema.sql")!!
                .bufferedReader()
                .readText()

        getConnection().use { connection ->
            connection.createStatement().use { statement ->
                // H2는 세미콜론 기준으로 분리해서 각각 실행
                ddl
                    .split(";")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .forEach { statement.execute(it) }
            }
        }
    }
}
