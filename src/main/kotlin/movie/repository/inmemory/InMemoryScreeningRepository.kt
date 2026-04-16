package movie.repository.inmemory

import movie.model.movie.Movie
import movie.model.screening.Screening
import movie.model.screening.ScreeningSeatMap
import movie.model.screening.Screenings
import movie.repository.ScreeningRepository
import movie.repository.inmemory.InMemoryMovieRepository.F1_THE_MOVIE
import movie.repository.inmemory.InMemoryMovieRepository.IRON_MAN
import movie.repository.inmemory.InMemoryMovieRepository.ROBOT_DREAM
import movie.repository.inmemory.InMemoryMovieRepository.TOY_STORY
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class InMemoryScreeningRepository(
    private var screenings: Screenings = Screenings(createScreenings()),
) : ScreeningRepository {
    override fun findBy(
        movie: Movie,
        date: LocalDate,
    ): Screenings = Screenings(screenings.filter { it.movie == movie && it.showDate == date })

    override fun findById(id: Long): Screening? = screenings.find { it.id == id }

    override fun getAllScreenings(): Screenings = Screenings(screenings.toList())

    fun update(newScreening: Screening) {
        screenings =
            Screenings(screenings.map { if (it.isSameScreening(newScreening)) newScreening else it })
    }

    companion object {
        private val BASE_DATE: LocalDate = LocalDate.of(2025, 9, 20)
        private val fMovie1 =
            Screening(
                movie = F1_THE_MOVIE,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(10, 20)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen1),
            )
        private val fMovie2 =
            Screening(
                movie = F1_THE_MOVIE,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(13, 0)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen1),
            )
        private val fMovie3 =
            Screening(
                movie = F1_THE_MOVIE,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(15, 40)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen1),
            )
        private val fMovie4 =
            Screening(
                movie = F1_THE_MOVIE,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(20, 10)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen1),
            )
        private val tMovie1 =
            Screening(
                movie = TOY_STORY,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(13, 30)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen2),
            )
        private val tMovie2 =
            Screening(
                movie = TOY_STORY,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(16, 0)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen2),
            )
        private val iMovie1 =
            Screening(
                movie = IRON_MAN,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(9, 50)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen3),
            )
        private val rMovie1 =
            Screening(
                movie = ROBOT_DREAM,
                startDateTime = LocalDateTime.of(BASE_DATE, LocalTime.of(21, 0)),
                seatMap = ScreeningSeatMap(InMemoryScreenRepository.screen3),
            )

        private fun createScreenings(): List<Screening> = listOf(fMovie1, fMovie2, fMovie3, fMovie4, tMovie1, tMovie2, iMovie1, rMovie1)
    }
}
