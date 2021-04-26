package element.challenge.api

import element.challenge.AbstractIntegrationTest
import element.challenge.persistence.TrailRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.util.*

internal class TrailRestControllerIntegrationTest @Autowired constructor(private val apiIntegrationTest: APIIntegrationTest, private val repository: TrailRepository) :
    AbstractIntegrationTest() {

    @Test
    fun `should retrieve all trails`() {
        val actual = apiIntegrationTest.getTrails<Array<TrailDTO>>()

        val expected = repository.findAll().map { it.toDTO() }
        assertThat(actual.body?.toList()).isEqualTo(expected)
    }

    @Test
    fun `should throw 404 when not trail is found by ID` () {
        val actual = apiIntegrationTest.getTrail<Any>(UUID.randomUUID())

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun retrieveTrailById() {
        val entity = repository.findAll().first()

        val actual = apiIntegrationTest.getTrail<TrailDTO>(entity.id!!)

        assertThat(actual.body).isEqualTo(entity.toDTO())
    }
}