package movie.domain.movie

class Theaters(
    theaters: List<Theater>,
) {
    private val value = theaters.toMutableList()

    fun addTheater(
        newTheater: Theater
    ) {
        value.add(newTheater)
    }
}
