package element.challenge.api

import element.challenge.api.BookingResource.Companion.BOOKING_URI
import element.challenge.api.HikerResource.Companion.HIKERS_URI
import element.challenge.api.TrailResource.Companion.TRAILS_URI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
internal class APIIntegrationTest @Autowired constructor(private val restTemplate: TestRestTemplate) {

    final inline fun <reified T> createHiker(newHiker: HikerDTO): ResponseEntity<T> =
        restTemplate.postForEntity(HIKERS_URI, newHiker, T::class.java)

    final inline fun <reified T> getHiker(id: UUID): ResponseEntity<T> =
        restTemplate.getForEntity("$HIKERS_URI/$id", T::class.java)

    final inline fun <reified T> getTrails(): ResponseEntity<T> = restTemplate.getForEntity(TRAILS_URI, T::class.java)

    final inline fun <reified T> getTrail(id: UUID): ResponseEntity<T> =
        restTemplate.getForEntity("$TRAILS_URI/$id", T::class.java)

    final inline fun <reified T> createBooking(bookingCreationRequest: BookingCreationRequest): ResponseEntity<T> =
        restTemplate.postForEntity(BOOKING_URI, bookingCreationRequest, T::class.java)

    final inline fun <reified T> getBooking(id: UUID): ResponseEntity<T> =
        restTemplate.getForEntity("$BOOKING_URI/$id", T::class.java)

    final fun cancelBooking(id: UUID) = restTemplate.delete("$BOOKING_URI/$id")
}