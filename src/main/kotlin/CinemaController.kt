import model.CinemaKiosk
import model.CinemaTime
import model.MovieReservationResult
import model.movie.MovieName
import model.schedule.MovieNameGroup
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import model.seat.SeatColumn
import model.seat.SeatRow
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
) {
    val movieNames =
        MovieNameGroup(
            listOf(
                MovieName("혼자사는남자", id = "1"),
            ),
        )

    fun run() {
        if (startMovieReservation().not()) return
        val movieSchedule = getMovieSchedule()
        val date = inputDate()
        OutputView.showMovieSchedule(movieSchedule)
        val movieScreening = selectMovieScreening(movieSchedule)
        OutputView.showSeatGroup(movieScreening.seatGroup)
        reserveSeats(movieScreening.movie.name, movieScreening.screenTime.start)
    }

    private fun startMovieReservation(): Boolean {
        while (true) {
            try {
                val start = InputView.startMovieReservation(Message.START_RESERVATION)
                return start
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun getMovieSchedule(): MovieSchedule {
        while (true) {
            val movieName = movieNames.find(InputView.inputMovieName())
            if (movieName != null) {
                val movieSchedule = cinemaKiosk.cinemaSchedule.getMovieSchedule(movieName)
                if (movieSchedule.isEmpty().not()) return movieSchedule
            }
        }
    }

    private fun inputDate(): CinemaTime {
        while (true) {
            try {
                return InputView.inputDate()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun selectMovieScreening(movieSchedule: MovieSchedule): MovieScreening {
        while (true) {
            try {
                return InputView.selectMovieScreening(movieSchedule)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    private fun reserveSeats(
        movieName: MovieName,
        startTime: CinemaTime,
    ) {
        while (true) {
            try {
                val positions = InputView.selectSeats()
                val successPositions = mutableListOf<Pair<SeatRow, SeatColumn>>()

                for (position in positions) {
                    val result =
                        cinemaKiosk.reserve(
                            movieName = movieName,
                            startTime = startTime,
                            seatRow = position.first,
                            seatColumn = position.second,
                        )
                    when (result) {
                        is MovieReservationResult.Success -> successPositions.add(position)
                        is MovieReservationResult.Failed -> {
                            cinemaKiosk.cancelReservations(movieName, startTime, successPositions)
                        }
                    }
                }
            } catch (_: Exception) {
                println("문제가 생겼으니 알아서 에러를 찾으십쇼?")
            }
        }
    }
}
