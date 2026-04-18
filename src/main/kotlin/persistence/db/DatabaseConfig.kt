package persistence.db

internal data class DatabaseConfig(
    val jdbcUrl: String,
    val username: String = "sa",
    val password: String = "",
)
