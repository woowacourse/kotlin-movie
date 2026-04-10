package view

object OutputView {
    fun showInvalidMovieName() {
        println(Message.INVALID_MOVIE_NAME)
    }

    fun showErrorMessage(message: String?) {
        println(message ?: "오류가 발생했습니다.")
    }

    fun end() {
        println("감사합니다.")
    }
}
