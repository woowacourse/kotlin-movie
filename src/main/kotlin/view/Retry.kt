package view

fun <T> retryUntilValid(action: () -> T): T {
    while (true) {
        try {
            return action()
        } catch (e: IllegalArgumentException) {
            OutputView.printError(e.message ?: "알 수 없는 예외가 발생했습니다.")
        }
    }
}
