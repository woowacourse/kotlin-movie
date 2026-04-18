package persistence

import domain.cinema.MovieTheater
import domain.purchase.Receipt
import domain.reservation.ReservationInfo
import persistence.db.DatabaseInitializer
import persistence.db.JdbcDatabase
import persistence.repository.JdbcMovieTheaterRepository
import persistence.repository.JdbcReservationBatchRepository
import persistence.repository.JdbcReservationRepository
import persistence.seed.CinemaSeedData
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

class CinemaDatabase private constructor(
    private val database: JdbcDatabase,
) {
    private val movieTheaterRepository = JdbcMovieTheaterRepository(database)
    private val reservationRepository = JdbcReservationRepository(database)
    private val reservationBatchRepository = JdbcReservationBatchRepository(database)

    init {
        DatabaseInitializer(database).initialize()
        movieTheaterRepository.initialize(CinemaSeedData.movieTheater())
    }

    fun loadMovieTheater(): MovieTheater = movieTheaterRepository.find()

    fun saveReservations(reservationInfos: List<ReservationInfo>) {
        reservationRepository.saveAll(reservationInfos)
    }

    fun save(receipt: Receipt) {
        save(UUID.randomUUID().toString(), receipt)
    }

    fun save(
        reservationId: String,
        receipt: Receipt,
    ) {
        database.withTransaction { connection ->
            reservationRepository.saveAll(connection, receipt.purchaseHistory)
            reservationBatchRepository.save(connection, reservationId, receipt)
        }
    }

    fun findReservations(): List<ReservationInfo> = reservationRepository.findAll()

    companion object {
        fun local(): CinemaDatabase {
            val databaseDirectory = Paths.get("storage")
            Files.createDirectories(databaseDirectory)
            return local(databaseDirectory.resolve("movie-ticketing"))
        }

        fun local(databasePath: Path): CinemaDatabase = CinemaDatabase(JdbcDatabase.file(databasePath))

        fun inMemory(name: String): CinemaDatabase = CinemaDatabase(JdbcDatabase.inMemory(name))
    }
}
