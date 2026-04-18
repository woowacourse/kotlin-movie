package controller

import domain.purchase.Receipt
import domain.reservation.Cart
import domain.user.User
import util.retryOnInvalidInput
import view.InputView
import view.OutputView

class PaymentController {
    fun run(
        cart: Cart,
        user: User,
    ): Receipt {
        var receipt = cart.issueReceipt()
        receipt = retryOnInvalidInput(OutputView::printError) { getUserPoint(receipt, user) }

        receipt = retryOnInvalidInput(OutputView::printError) { getPaymentMethod(receipt) }

        OutputView.printTotalPrice(receipt.totalPrice())

        return receipt
    }

    fun createReceipt(cart: Cart): Receipt = cart.issueReceipt()

    fun getUserPoint(
        receipt: Receipt,
        user: User,
    ): Receipt {
        val input = InputView.readPoint()
        return receipt.applyPoint(user, input)
    }

    fun getPaymentMethod(receipt: Receipt): Receipt = receipt.applyPaymentMethod(InputView.readPaymentMethod())

    fun confirmPayment(
        user: User,
        receipt: Receipt,
    ) = receipt.confirm(user)
}
