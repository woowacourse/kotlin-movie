package domain.user

class Point(val point: Int) {
    fun discount(discount: Int): Point {
        require(point - discount >= 0) { "차감액은 전체 포인트보다 작아야 합니다." }

        return Point(point - discount)
    }
}
