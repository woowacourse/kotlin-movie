package persistence.repository

import domain.purchase.Receipt
import persistence.ScreeningIdGenerator
import persistence.db.JdbcDatabase
import java.sql.Connection

internal class JdbcReservationBatchRepository(
    private val database: JdbcDatabase,
) {
    fun save(
        reservationId: String,
        receipt: Receipt,
    ) {
        database.withTransaction { connection ->
            save(connection, reservationId, receipt)
        }
    }

    fun save(
        connection: Connection,
        reservationId: String,
        receipt: Receipt,
    ) {
        connection
            .prepareStatement(
                """
                INSERT INTO reservation_batches (id, used_points, payment_method, total_price)
                VALUES (?, ?, ?, ?)
                """.trimIndent(),
            ).use { statement ->
                statement.setString(1, reservationId)
                statement.setInt(2, receipt.usedPoint)
                statement.setString(3, requireNotNull(receipt.paymentMethod).name)
                statement.setInt(4, receipt.totalPrice())
                statement.executeUpdate()
            }

        connection
            .prepareStatement(
                """
                INSERT INTO reservation_batch_items (reservation_batch_id, screening_id, seat_row, seat_column)
                VALUES (?, ?, ?, ?)
                """.trimIndent(),
            ).use { statement ->
                receipt.purchaseHistory.forEach { reservationInfo ->
                    statement.setString(1, reservationId)
                    statement.setString(2, ScreeningIdGenerator.generate(reservationInfo.screening))
                    statement.setString(
                        3,
                        reservationInfo.seat.coordinate.row
                            .toString(),
                    )
                    statement.setInt(4, reservationInfo.seat.coordinate.column)
                    statement.addBatch()
                }
                statement.executeBatch()
            }
    }
}
