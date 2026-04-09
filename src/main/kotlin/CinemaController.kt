import model.CinemaKiosk
import model.CinemaTime
import model.MovieReservationResult
import model.movie.MovieName
import model.schedule.MovieNameGroup
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import view.InputView
import view.OutputView

class CinemaController(
    val cinemaKiosk: CinemaKiosk,
) {
    val movieNames =
        MovieNameGroup(
            listOf(
                MovieName("혼자사는남자", id = "1"),
                MovieName("F4 꽃보다 남자", id = "2"),
            ),
        )

    fun run() {
        if (startMovieReservation().not()) return
        do {
            val movieSchedule = getMovieSchedule()
            val date = inputDate()
            OutputView.showMovieSchedule(movieSchedule)
            val movieScreening = selectMovieScreening(movieSchedule)
            OutputView.showSeatGroup(movieScreening.seatGroup)
            reserveSeats(movieScreening.movie.name, movieScreening.screenTime.start)
        } while (inputContinue())
        OutputView.showShoppingCart(cinemaKiosk.reserveResults)
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
                val successPositions = mutableListOf<MovieReservationResult.Success>()

                for (position in positions) {
                    val result =
                        cinemaKiosk.reserve(
                            movieName = movieName,
                            startTime = startTime,
                            seatRow = position.first,
                            seatColumn = position.second,
                        )
                    when (result) {
                        is MovieReservationResult.Success -> successPositions.add(result)
                        is MovieReservationResult.Failed -> {
                            val successPositions = successPositions.map { it.seat.row to it.seat.column }
                            cinemaKiosk.cancelReservations(movieName, startTime, successPositions)
                            throw IllegalArgumentException("예약 실패했습니다? 잘 좀 하시죠?")
                        }
                    }
                }
                OutputView.showReservationInfo(successPositions)
                return
            } catch (_: Exception) {
                println("문제가 생겼으니 알아서 에러를 찾으십쇼?")
            }
        }
    }

    private fun inputContinue(): Boolean {
        while (true) {
            try {
                return InputView.inputContinue()
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }
}
