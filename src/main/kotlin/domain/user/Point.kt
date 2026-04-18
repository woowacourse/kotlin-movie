package domain.user

import util.ErrorMessage

class Point(
    val value: Int,
) {
    fun previewUsage(
        totalPrice: Int,
        input: String,
    ): Pair<Int, Int> {
        val usedPoint = input.toIntOrNull()
        require(usedPoint != null && usedPoint >= 0) { ErrorMessage.INVALID_POINT_INPUT }
        discount(usedPoint)

        return totalPrice - usedPoint to usedPoint
    }

    fun discount(discount: Int): Point {
        require(value - discount >= 0) { ErrorMessage.POINT_DEDUCTION_EXCEEDS_BALANCE }

        return Point(value - discount)
    }
}
