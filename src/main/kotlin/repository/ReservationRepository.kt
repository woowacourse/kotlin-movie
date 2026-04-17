package repository

import db.DatabaseConfig
import model.cart.Cart
import model.discount.PaymentMethod

class ReservationRepository(
    private val screeningRepository: ScreeningRepository =
        ScreeningRepository(),
) {
    fun save(
        cart: Cart,
        paymentMethod: PaymentMethod,
        usedPoint: Int,
        totalPrice: Int,
    ): Long {
        DatabaseConfig.getConnection().use { connection ->
            connection.autoCommit = false // 트랜잭션 시작
            try {
                // 1. RESERVATION 저장
                val reservationId =
                    insertReservation(
                        connection,
                        paymentMethod,
                        usedPoint,
                        totalPrice,
                    )

                // 2. RESERVATION_ITEM 저장 (CartItem별 좌석)
                cart.items.forEach { item ->
                    val screeningId =
                        screeningRepository
                            .findIdByMovieAndStartTime(item.screening)
                    item.seatNames.forEach { seatName ->
                        insertReservationItem(
                            connection,
                            reservationId,
                            screeningId,
                            seatName,
                        )
                    }
                }

                connection.commit()
                return reservationId
            } catch (e: Exception) {
                connection.rollback()
                throw e
            }
        }
    }

    private fun insertReservation(
        connection: java.sql.Connection,
        paymentMethod: PaymentMethod,
        usedPoint: Int,
        totalPrice: Int,
    ): Long {
        val sql = "INSERT INTO RESERVATION (payment_method, used_point, total_price) VALUES (?, ?, ?)"
        connection
            .prepareStatement(
                sql,
                java.sql.Statement.RETURN_GENERATED_KEYS,
            ).use { stmt ->
                stmt.setString(1, paymentMethod.name)
                stmt.setInt(2, usedPoint)
                stmt.setInt(3, totalPrice)
                stmt.executeUpdate()
                val keys = stmt.generatedKeys
                if (keys.next()) return keys.getLong(1)
            }
        throw IllegalStateException("예매 저장에 실패했습니다.")
    }

    private fun insertReservationItem(
        connection: java.sql.Connection,
        reservationId: Long,
        screeningId: Long,
        seatName: String,
    ) {
        val sql = "INSERT INTO RESERVATION_ITEM (reservation_id, screening_id, seat_name) VALUES (?, ?, ?)"
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, reservationId)
            stmt.setLong(2, screeningId)
            stmt.setString(3, seatName)
            stmt.executeUpdate()
        }
    }
}
