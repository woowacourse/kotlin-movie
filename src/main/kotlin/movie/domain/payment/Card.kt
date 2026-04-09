package movie.domain.payment

class Card : PaymentMethod {
    override val name = "신용카드"
    override val discountRate: Float = 0.05f
}
