package controller

class FlowController {
    fun start(input: String): Boolean {
        require(input == "Y" || input == "N") { "입력값은 Y 혹은 N이어야 합니다." }

        when (input) {
            "Y" -> return true
        }
        return false
    }
}
