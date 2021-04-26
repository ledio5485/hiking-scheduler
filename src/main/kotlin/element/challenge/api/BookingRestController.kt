package element.challenge.api

import element.challenge.logic.BookingService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class BookingRestController(private val bookingService: BookingService) : BookingResource {

    override fun createBooking(bookingCreationRequest: BookingCreationRequest) =
        with(bookingService.createBooking(bookingCreationRequest)) {
            ResponseEntity.status(HttpStatus.CREATED).body(toDTO())
        }

    override fun retrieveBookingById(id: UUID) =
        with(bookingService.retrieveBookingById(id).map { it.toDTO() }) {
            ResponseEntity.of(this)
        }

    override fun getBookings(pageable: Pageable): Page<BookingDTO> {
        with(bookingService.getBookings(pageable)) {
            return PageImpl(this.content.map { it.toDTO() }, pageable, this.totalElements)
        }
    }

    override fun cancelBooking(id: UUID) = bookingService.cancelBooking(id)
}