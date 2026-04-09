package domain.user

class Point(val value: Int) {
    fun discount(discount: Int): Point {
        require(value - discount >= 0) { "차감액은 전체 포인트보다 작아야 합니다." }

        return Point(value - discount)
    }
}
