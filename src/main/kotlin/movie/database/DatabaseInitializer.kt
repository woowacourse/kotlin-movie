package movie.database

import movie.error.SystemErrorMessage

object DatabaseInitializer {
    fun initSchema() {
        val connection = DatabaseFactory.getConnection()
        val statement = connection.createStatement()

        val checkTableResultSet = connection.metaData.getTables(null, null, "MOVIE", null)
        val isFirstRun = !checkTableResultSet.next()

        if (isFirstRun) {
            val schemaSql = this::class.java.classLoader.getResource("movie.sql")?.readText()
                ?: throw IllegalStateException("movie.sql" + SystemErrorMessage.SQL_FILE_NOT_FOUND)
            statement.execute(schemaSql)

            val dataSql = this::class.java.classLoader.getResource("movie_data.sql")?.readText()
                ?: throw IllegalStateException("movie_data.sql" + SystemErrorMessage.SQL_FILE_NOT_FOUND)
            statement.execute(dataSql)
        }

        statement.close()
        connection.close()
    }
}
