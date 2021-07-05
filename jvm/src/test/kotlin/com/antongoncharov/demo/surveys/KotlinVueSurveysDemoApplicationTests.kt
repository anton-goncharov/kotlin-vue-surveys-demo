package com.antongoncharov.demo.surveys

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class KotlinVueSurveysDemoApplicationTests {

	@Test
	fun contextLoads() {
	}

    // TODO implement rsocket test
    //  https://spring.io/blog/2020/05/25/getting-started-with-rsocket-testing-spring-boot-responders
    // https://spring.io/blog/2021/06/02/wiremock-for-rsocket

}
