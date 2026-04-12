package model.cart

import model.schedule.Screening

data class CartItem(
    val screening: Screening,
    val seatNames: List<String>,
)
