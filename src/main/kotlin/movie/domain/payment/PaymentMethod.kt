package movie.domain.payment

sealed interface PaymentMethod {
    data object CreditCard : PaymentMethod

    data object Cash : PaymentMethod
}
