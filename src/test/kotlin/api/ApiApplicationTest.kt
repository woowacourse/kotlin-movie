package api

import database.Database
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTest(
    @param:LocalServerPort private val port: Int,
) {
    private lateinit var client: RestTestClient

    @BeforeEach
    fun setUp() {
        Database.init(url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
        Database.connection().use { connection ->
            connection.createStatement().use {
                it.execute("DELETE FROM reservation_seat")
                it.execute("DELETE FROM reservation")
                it.execute("DELETE FROM movie_screening")
                it.execute("DELETE FROM movie")
            }
        }
        database.DataInitializer.initialize()
        client =
            RestTestClient
                .bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    fun `영화 목록을 조회한다`() {
        client
            .get()
            .uri("/api/movies")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.movies")
            .isArray()
    }

    @Test
    fun `예매를 생성한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                    "reservations" : [
                        {
                            "screeningId": 101,
                            "seats": ["A1", "A2"]
                        }
                    ],
                    "usedPoints": 2000,
                    "paymentMethod": "CREDIT_CARD"
                }
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.reservationId")
            .exists()
            .jsonPath("$.totalPrice")
            .exists()
    }

    @Test
    fun `이미 예약된 좌석을 예매하려는 경우 오류 응답을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """                                                                                                                                                                                                                                                                                                           
                {   
                    "reservations" : [          
                        {                   
                            "screeningId": 101,
                            "seats": ["A1"]                                                                                                                                                                                                                                                                                   
                        }
                    ],                                                                                                                                                                                                                                                                                                        
                    "usedPoints": 0,
                    "paymentMethod": "CREDIT_CARD"
                }                               
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isCreated()

        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """ 
                {
                    "reservations" : [
                        {
                            "screeningId": 101,
                            "seats": ["A1"]     
                        }                   
                    ],
                    "usedPoints": 0,                                                                                                                                                                                                                                                                                          
                    "paymentMethod": "CREDIT_CARD"
                }                                                                                                                                                                                                                                                                                                             
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
    }

    @Test
    fun `존재하지 않는 상영에 대해 예매를 요청하면 적절한 오류 응답을 반환한다`() {
        client
            .post()
            .uri("/api/reservations")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """                                                                                                                                                                                                                                                                                                           
                {                                                                                                                                                                                                                                                                                                             
                    "reservations" : [
                        {
                            "screeningId": 99999,
                            "seats": ["A1"]     
                        }                   
                    ],
                    "usedPoints": 0,                                                                                                                                                                                                                                                                                          
                    "paymentMethod": "CREDIT_CARD"
                }                                                                                                                                                                                                                                                                                                             
                """.trimIndent(),
            ).exchange()
            .expectStatus()
            .isBadRequest()
    }
}
