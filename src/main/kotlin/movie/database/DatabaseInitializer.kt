package movie.database

import java.sql.Connection
import kotlin.use

object DatabaseInitializer {
    fun initTables(isLocal: Boolean) {
        val connection = DatabaseConnectionFactory.createConnection(isLocal)

        connection.use { conn ->
            createTables(conn)
            insertData(conn)
        }
    }

    private fun createTables(conn: Connection) {
        // 1. 초기 movie 테이블 없으면 생성
        conn.createStatement().use { statement ->
            val sql =
                """
                CREATE TABLE IF NOT EXISTS movie (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255),
                running_time BIGINT
                )
                """.trimIndent()

            statement.execute(sql)
        }

        // 2. 초기 screening 테이블 없으면 생성
        conn.createStatement().use { statement ->
            val sql =
                """
                CREATE TABLE IF NOT EXISTS screening (
                id BIGINT AUTO_INCREMENT(101, 1) PRIMARY KEY,
                movie_id BIGINT,
                start_date_time TIMESTAMP,
                screen_name VARCHAR(255)
                )
                """.trimIndent()

            statement.execute(sql)
        }

        // 3. 초기 reservation 테이블 없으면 생성
        conn.createStatement().use { statement ->
            val sql =
                """
                CREATE TABLE IF NOT EXISTS reservation (
                screening_id BIGINT,
                seats VARCHAR(255)
                )
                """.trimIndent()

            statement.execute(sql)
        }

        // 4. 초기 reserved_seat 테이블 없으면 생성
        conn.createStatement().use { statement ->
            val sql =
                """
                CREATE TABLE IF NOT EXISTS reserved_seat (
                screening_id BIGINT,
                reserved_seats VARCHAR(255)
                )
                """.trimIndent()

            statement.execute(sql)
        }
    }

    private fun insertData(conn: Connection) {
        // 1. movie 테이블 내에 데이터가 없으면 기본 영화 4편 세팅
        conn.createStatement().use { statement ->
            val sql =
                """
                SELECT count(*) FROM movie
                """.trimIndent()

            val resultSet = statement.executeQuery(sql)
            if (resultSet.next() && resultSet.getLong(1) == 0L) {
                println("DB가 비어있습니다. 초기 영화 세팅을 시작합니다.")

                conn.createStatement().use { insertStatement ->
                    val insertSql =
                        """
                        INSERT INTO movie (title, running_time)
                        VALUES
                            ('F1 더 무비', 160),
                            ('토이 스토리', 150),
                            ('아이언맨', 130),
                            ('로봇 드림', 110)
                        """.trimIndent()

                    insertStatement.execute(insertSql)
                }
            }
        }

        // 2. screening 테이블 내에 데이터가 없으면 기본값 세팅
        conn.createStatement().use { statement ->
            val sql =
                """
                SELECT count(*) FROM screening
                """.trimIndent()

            val resultSet = statement.executeQuery(sql)

            if (resultSet.next() && resultSet.getLong(1) == 0L) {
                println("DB에 회차 정보가 없습니다. 초기 세팅을 시작합니다.")

                conn.createStatement().use { insertStatement ->
                    val insertSql =
                        """
                        INSERT INTO screening (movie_id, start_date_time, screen_name)
                        VALUES
                        (1, '2025-09-20 10:20:00', '1관'),
                        (1, '2025-09-20 13:00:00', '1관'),
                        (1, '2025-09-20 15:40:00', '1관'),
                        (1, '2025-09-20 20:10:00', '1관'),
                        (2, '2025-09-20 13:30:00', '2관'),
                        (2, '2025-09-20 16:00:00', '2관'),
                        (3, '2025-09-20 09:50:00', '3관'),
                        (4, '2025-09-20 21:00:00', '3관')
                        """.trimIndent()

                    insertStatement.execute(insertSql)
                }
            }
        }
    }
}
