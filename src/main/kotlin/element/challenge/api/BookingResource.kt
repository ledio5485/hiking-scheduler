package element.challenge.api

import element.challenge.api.BookingResource.Companion.BOOKING_URI
import element.challenge.persistence.BookingEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*
import javax.validation.Valid

@RequestMapping(BOOKING_URI)
interface BookingResource {
    companion object {
        const val BOOKING_URI = "/api/booking"
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun createBooking(@RequestBody @Valid bookingCreationRequest: BookingCreationRequest): ResponseEntity<BookingDTO>

    @GetMapping
    @ResponseBody
    fun getBookings(@RequestBody @Valid pageable: Pageable): Page<BookingDTO>

    @GetMapping("/{id}")
    @ResponseBody
    fun retrieveBookingById(@PathVariable("id") id: UUID): ResponseEntity<BookingDTO>

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelBooking(@PathVariable("id") id: UUID)
}