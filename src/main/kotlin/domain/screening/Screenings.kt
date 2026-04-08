package domain.screening

class Screenings(private val value: List<Screening>) : Iterable<Screening> {
    override fun iterator(): Iterator<Screening> = value.iterator()

    fun isEmpty(): Boolean = value.isEmpty()
}
