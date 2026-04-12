import controller.Controller
import domain.screening.Scheduler

fun main() {
    Controller(Scheduler.createSchedule()).run()
}
