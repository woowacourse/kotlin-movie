package view

object InputView {
    fun inputUserQuery(text: String): String {
        println(text)
        return readln()
    }

    fun startMovieReservation(text: String): Boolean {
        println(text)
        val input = readln()
        InputValidator.validateYesOrNo(input)
        return input == "Y"
    }

    fun inputMovieName(): String {
        println(Message.INPUT_MOVIE_NAME)
        return readln()
    }
}
