package movie.domain.payment

class Card : PaymentMethod {
    override val discountRate: Float = 0.05f
}
