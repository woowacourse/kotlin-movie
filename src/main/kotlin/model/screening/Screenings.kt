package model.screening

class Screenings(
    screenings: List<Screening>,
) : Iterable<Screening> {
    private val value = screenings.toList()

    override fun iterator(): Iterator<Screening> = value.iterator()

    fun isEmpty(): Boolean = value.isEmpty()
}
