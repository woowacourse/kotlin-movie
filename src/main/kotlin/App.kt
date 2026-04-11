import controller.CinemaController
import view.SeatLayoutView
import java.time.LocalDate

fun main() {
    val controller = CinemaController()

    // 영화 제목을 입력 받고 해당 제목 영화의 상영 목록
    println("영화 제목 입력해")
    val title = readln()
    val screenings = controller.findScreeningTitle(title)

    // 영화 날짜를 입력 받고 제목으로 필터링 된 리스트 중 해당 날짜 리스트 반환
    println("날짜 입력해")
    val date = readln().split("-").map { it.toInt() }
    val formattingDate = LocalDate.of(date[0], date[1], date[2])
    val findTitleAndDateSchedule= controller.findScreeningsDate(screenings, formattingDate)

    // 영화 날짜를 입력 받고 제목으로 필터링 된 리스트 중 상영 시간들만 조회
    println("해당 날짜의 상영 목록")
    val findScheduleTimeList = controller.findScreeningsTime(findTitleAndDateSchedule)
    findScheduleTimeList.forEachIndexed { index, time ->
        println("[${index + 1}] $time")
    }
}
