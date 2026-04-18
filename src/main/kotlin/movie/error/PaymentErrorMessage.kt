package movie.error

object PaymentErrorMessage {
    const val INVALID_PRICE = "가격은 0원 이상이어야 합니다."
    const val INVALID_POINT = "포인트는 0원 이상이어야 합니다."
    const val POINT_NOT_INTEGER = "포인트는 정수여야 합니다."
    const val INVALID_METHOD = "올바르지 않은 결제 수단입니다."
}
