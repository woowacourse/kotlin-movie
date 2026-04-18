package model.movie

import model.CinemaTimeRange
import java.util.Objects

class Movie(
    private val name: MovieName,
    private val id: MovieId,
    private val runningTime: RunningTime,
) {
    fun isSameDuration(cinemaTimeRange: CinemaTimeRange): Boolean = runningTime.isSameDuration(cinemaTimeRange)

    fun isSameName(name: MovieName): Boolean = this.name == name

    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.id == other.id && this.name == other.name
        }
        return false
    }

    override fun toString(): String = name.toString()

    override fun hashCode(): Int = Objects.hash(id, name)
}
