package movie.domain.payment

class Cash : PaymentMethod {
    override val discountRate: Float = 0.02f
}
