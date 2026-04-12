package model.discount.payDiscountPolicy

// 포인트 결제 방식
class PointPayDiscountPolicy(
    private val usePoint: Int,
) : PayDiscountPolicy {
    override fun calculatePrice(price: Int): Int {
        require(usePoint <= price) { "포인트 사용 금액은 결제 금액보다 클 수 없습니다" }
        return price - usePoint
    }
}
