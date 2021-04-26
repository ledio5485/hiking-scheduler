package element.challenge.api

import element.challenge.AbstractIntegrationTest
import element.challenge.config.ErrorResponse
import element.challenge.persistence.TrailRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*

internal class BookingRestControllerIntegrationTest @Autowired constructor(
    private val apiIntegrationTest: APIIntegrationTest,
    private val trailRepository: TrailRepository
) : AbstractIntegrationTest() {

    @Test
    internal fun `should create booking`() {
        val trail = trailRepository.findAll().first()
        val newHiker =
            HikerDTO(null, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")
        val hiker = apiIntegrationTest.createHiker<HikerDTO>(newHiker).body!!
        val bookingCreationRequest =
            BookingCreationRequest(trailId = trail.id!!, hikerIds = setOf(hiker.id!!), LocalDate.now().plusDays(1))

        val actual = apiIntegrationTest.createBooking<BookingDTO>(bookingCreationRequest)

        val expected = BookingDTO(actual.body!!.id, trail.toDTO(), setOf(hiker), LocalDate.now().plusDays(1))
        assertThat(actual.body).isEqualTo(expected)
    }

    @Test
    internal fun `should return 404 NOT_FOUND when looking for a cancelled booking`() {
        val trail = trailRepository.findAll().first()
        val newHiker =
            HikerDTO(null, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")
        val hiker = apiIntegrationTest.createHiker<HikerDTO>(newHiker).body!!
        val bookingCreationRequest =
            BookingCreationRequest(trailId = trail.id!!, hikerIds = setOf(hiker.id!!), LocalDate.now().plusDays(1))
        val booking = apiIntegrationTest.createBooking<BookingDTO>(bookingCreationRequest).body!!
        apiIntegrationTest.cancelBooking(booking.id)

        val actual = apiIntegrationTest.getBooking<Any>(UUID.randomUUID())

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    internal fun `should return 404 NOT_FOUND when looking for non existent booking`() {
        val actual = apiIntegrationTest.getBooking<Any>(UUID.randomUUID())

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    internal fun `should throw 400 BAD_REQUEST when a hiker wants to participate in a trail that is not allowed due to age restriction`() {
        val trail = trailRepository.findAll().first()
        val newHiker =
            HikerDTO(null, "name", "surname", LocalDate.now().minusYears(1), "+1 234 567890", "name@surname.element")
        val hiker = apiIntegrationTest.createHiker<HikerDTO>(newHiker).body!!
        val bookingCreationRequest = BookingCreationRequest(trailId = trail.id!!, hikerIds = setOf(hiker.id!!), date = LocalDate.now().plusDays(1L))

        val actual = apiIntegrationTest.createBooking<ErrorResponse>(bookingCreationRequest)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(actual.body?.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
        assertThat(actual.body?.message).contains("The hiker ${hiker.id} did not met the minimum requirement of participation (age between ${trail.minimumAge} and ${trail.maximumAge})")
    }

    @Test
    internal fun `should get 404 NOT_FOUND when looking for a nonexistent the booking by ID`() {
        val actual = apiIntegrationTest.getBooking<BookingDTO>(UUID.randomUUID())

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    internal fun `should get the booking by ID`() {
        val trail = trailRepository.findAll().first()
        val newHiker =
            HikerDTO(null, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")
        val hiker = apiIntegrationTest.createHiker<HikerDTO>(newHiker).body!!
        val bookingCreationRequest = BookingCreationRequest(trailId = trail.id!!, hikerIds = setOf(hiker.id!!), date = LocalDate.now().plusDays(1))
        val expected = apiIntegrationTest.createBooking<BookingDTO>(bookingCreationRequest).body!!

        val actual = apiIntegrationTest.getBooking<BookingDTO>(expected.id)

        assertThat(actual.body).isEqualTo(expected)
    }

    @Test
    internal fun `should cancel a booking`() {
        val trail = trailRepository.findAll().first()
        val newHiker =
            HikerDTO(null, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")
        val hiker = apiIntegrationTest.createHiker<HikerDTO>(newHiker).body!!
        val bookingCreationRequest =
            BookingCreationRequest(trailId = trail.id!!, hikerIds = setOf(hiker.id!!), date = LocalDate.now().plusDays(1))
        val booking = apiIntegrationTest.createBooking<BookingDTO>(bookingCreationRequest).body!!

        apiIntegrationTest.cancelBooking(booking.id)

        val actual = apiIntegrationTest.getBooking<Any>(booking.id)

        assertThat(actual.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}