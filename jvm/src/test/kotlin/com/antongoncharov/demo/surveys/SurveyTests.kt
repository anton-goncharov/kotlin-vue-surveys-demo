package com.antongoncharov.demo.surveys

import com.antongoncharov.demo.surveys.model.Survey
import com.antongoncharov.demo.surveys.persistence.SurveyRepository
import com.antongoncharov.demo.surveys.security.JwtTokenUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles

// with Junit 5, we do not need @RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SurveyTests(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val jwtTokenUtil: JwtTokenUtil,
    @Autowired private val surveyRepository: SurveyRepository
) {

    @Test
    fun `created survey can be queried and deleted`() {
        val body = """
            {
                "title": "New Survey"
            }
        """.trimIndent();
        val httpEntity = authorizedHttpEntity(jwtTokenUtil, body)
        val response = restTemplate.exchange<String>("/api/v1/surveys", HttpMethod.POST, httpEntity)

        println(response)
        assertThat(response.statusCode.value()).isEqualTo(201)
        val location = response.headers.location
        assertThat(location).isNotNull()
        val createdUuid = location!!.path.split('/').last()

        // check that it has been created
        val findOne = restTemplate.exchange<Map<String,Any>>("/api/v1/surveys/$createdUuid", HttpMethod.GET, authorizedHttpEntity(jwtTokenUtil))
        assertThat(findOne.statusCode.is2xxSuccessful).isTrue()

        // delete it
        val deleted = restTemplate.exchange<Map<String,Any>>("/api/v1/surveys/$createdUuid", HttpMethod.DELETE, authorizedHttpEntity(jwtTokenUtil))
        assertThat(deleted.statusCode.is2xxSuccessful).isTrue()
    }

    @Test
    @WithMockUser(username = "testuser@mail.test", authorities = ["ROLE_COORDINATOR"])
    fun `list surveys should return data`() {

        val surveys = mutableListOf(
            Survey(title = "Survey 1"),
            Survey(title = "Survey 2")
        )
        surveyRepository.saveAll(surveys);

        val entity = restTemplate.exchange<Map<String,Any>>("/api/v1/surveys", HttpMethod.GET, authorizedHttpEntity(jwtTokenUtil))
        println(entity.body)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isNotEmpty
        val embedded = entity.body!!["_embedded"] as Map<*, *>
        val surveyList = embedded["surveys"] as List<*>
        assertThat(surveyList.size).isEqualTo(2)
    }

}