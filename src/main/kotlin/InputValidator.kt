import model.payment.PayType

object InputValidator {
    fun validateYesOrNo(input: String) {
        require(input == "Y" || input == "N") { Message.INVALID_YES_OR_NO }
    }

    fun validateNumber(input: String) {
        require(input.toIntOrNull() != null) { Message.IS_NOT_DIGIT }
        require(input.toInt() >= 0) { Message.IS_NEGATIVE_NUMBER }
    }

    fun validateType(input: String) {
        require(input.toIntOrNull() != null) { Message.IS_NOT_NUMBER }
        require(input.toInt() in 1..PayType.entries.size) { Message.INVALID_PAY_TYPE }
    }
}
