import model.payment.PayType

object InputValidator {
    fun validateYesOrNo(input: String) {
        require(input == "Y" || input == "N") { "Y, N만 입력 가능합니다." }
    }

    fun validateNumber(input: String) {
        require(input.toIntOrNull() != null) { "숫자만 입력 가능합니다." }
        require(input.toInt() >= 0) { "0 이상만 입력 가능합니다." }
    }

    fun validateType(input: String) {
        require(input.toIntOrNull() != null) { "숫자만 입력 가능합니다." }
        require(input.toInt() in 1..PayType.entries.size) { "존재하지 않는 번호입니다." }
    }
}
