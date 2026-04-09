import controller.CinemaController
import java.time.LocalDate

fun main() {
    val controller = CinemaController()

    println("영화 제목 입력해")
    val title = readln()

    val screenings = controller.findScreeningTitle(title)

    println("날짜 입력해")
    val date = readln().split("-").map { it.toInt() }
    val formattingDate = LocalDate.of(date[0], date[1], date[2])

    // 제목과 날짜가 동일한 스크린의 목록
    controller.findScreeningsDate(screenings, formattingDate).forEach {
        println("제목 : ${it.movie}")
        println("영화 시작 시간 : ${it.startTime}")
        println("영화 종료 시간 : ${it.endTime}")
    }
}
