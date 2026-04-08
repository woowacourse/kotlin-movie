package domain

data class Movie(
    val title: String,
    val runningMinutes: Int,
){
    companion object{
        val sampleMovies: List<Movie> = listOf(
            Movie("탑건: 매버릭", 130),
            Movie("마더", 100),
            Movie("스파이더맨: 노 웨이 홈", 140),
            Movie("남은 인생 10년", 124),
            Movie("아이언맨 3", 122),
            Movie("오늘 밤 이세상에서 사랑이 사라진다 해도", 105),
            Movie("체인소맨", 101),
            Movie("호퍼스", 105),
        )
    }
}




