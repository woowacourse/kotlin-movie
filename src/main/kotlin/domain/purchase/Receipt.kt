package domain.purchase

import domain.reservation.ReservationInfo
import domain.user.User

class Receipt(
    val purchaseHistory: List<ReservationInfo>,
    val usedPoint: Int = 0,
    val paymentMethod: PaymentMethod? = null,
) {
    fun applyPoint(
        user: User,
        input: String,
    ): Receipt {
        val (_, usedPoint) = user.previewPointUsage(basePrice(), input)
        return Receipt(
            purchaseHistory = purchaseHistory,
            usedPoint = usedPoint,
            paymentMethod = paymentMethod,
        )
    }

    fun applyPaymentMethod(method: PaymentMethod): Receipt =
        Receipt(
            purchaseHistory = purchaseHistory,
            usedPoint = usedPoint,
            paymentMethod = method,
        )

    fun confirm(user: User) {
        user.discountPoint(usedPoint)
    }

    fun totalPrice(): Int {
        val discountedByPoint = basePrice() - usedPoint
        return paymentMethod?.applyDiscount(discountedByPoint) ?: discountedByPoint
    }

    private fun basePrice(): Int = purchaseHistory.sumOf(ReservationInfo::price)
}
