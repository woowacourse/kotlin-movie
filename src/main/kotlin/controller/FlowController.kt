package controller

import view.InputView

class FlowController {
    fun start(input: String): Boolean {
        var input = InputView.startTicketing()
        require(input == "Y" || input == "N") { "입력값은 Y 혹은 N이어야 합니다." }

        when (input) {
            "Y" -> return true
        }
        return false
    }
}
