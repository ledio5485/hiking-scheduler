package element.challenge.api

import element.challenge.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*

internal class HikerRestControllerIntegrationTest @Autowired constructor(private val apiIntegrationTest: APIIntegrationTest): AbstractIntegrationTest() {

    @Test
    internal fun `should create a new hiker`() {
        val newHiker = HikerDTO(null, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")

        val actual = apiIntegrationTest.createHiker<HikerDTO>(newHiker)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(actual.body).isEqualTo(newHiker.copy(id = actual.body!!.id))
    }

    @Test
    internal fun `should return 404 NOT_FOUND when looking for non existent hiker`() {
        val actual = apiIntegrationTest.getHiker<Any>(UUID.randomUUID())

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    internal fun `should get the hiker by ID`() {
        val newHiker = HikerDTO(null, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")
        val hiker = apiIntegrationTest.createHiker<HikerDTO>(newHiker).body!!

        val actual = apiIntegrationTest.getHiker<HikerDTO>(hiker.id!!)

        assertThat(actual.body).isEqualTo(hiker)
    }
}