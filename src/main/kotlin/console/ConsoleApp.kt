package console

import console.controller.MovieController
import db.DataSeeder
import db.DatabaseConfig

fun main() {
    DatabaseConfig.initialize()
    DataSeeder.seed()
    MovieController().run()
}
