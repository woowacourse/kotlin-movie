package database.default

object DefaultScreenings {
    data class ScreeningRow(
        val id: Int,
        val movieId: Int,
        val startAt: String,
        val endAt: String,
    )

    val rows: List<ScreeningRow> =
        listOf(
            ScreeningRow(
                id = 101,
                movieId = 1,
                startAt = "2026-04-16T13:30:00",
                endAt = "2026-04-16T16:19:00",
            ),
            ScreeningRow(
                id = 102,
                movieId = 1,
                startAt = "2026-04-16T18:00:00",
                endAt = "2026-04-16T20:49:00",
            ),
            ScreeningRow(
                id = 201,
                movieId = 2,
                startAt = "2026-04-16T10:00:00",
                endAt = "2026-04-16T13:00:00",
            ),
        )
}
