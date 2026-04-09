object InputValidator {
    fun validateYesOrNo(input: String) {
        require(input == "Y" || input == "N") { "Y, N만 입력 가능합니다." }
    }
}
