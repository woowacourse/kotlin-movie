import view.InputView

class CinemaController {
    fun run() {
        if (startMovieReservation().not()) return
    }

    fun startMovieReservation(): Boolean {
        while (true) {
            try {
                val start = InputView.startMovieReservation(Message.START_RESERVATION)
                return start
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}
