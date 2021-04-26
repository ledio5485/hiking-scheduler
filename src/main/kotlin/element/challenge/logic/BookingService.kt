package element.challenge.logic

import element.challenge.api.BookingCreationRequest
import element.challenge.persistence.BookingEntity
import element.challenge.persistence.BookingRepository
import element.challenge.persistence.HikerEntity
import element.challenge.persistence.HikerRepository
import element.challenge.persistence.TrailEntity
import element.challenge.persistence.TrailRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

private val logger = KotlinLogging.logger {}

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val hikerRepository: HikerRepository,
    private val trailRepository: TrailRepository
) {

    fun createBooking(bookingCreationRequest: BookingCreationRequest): BookingEntity {
        logger.info { "The booking creation request $bookingCreationRequest was received." }

        val trail = trailRepository.findById(bookingCreationRequest.trailId).orElseThrow { TrailNotFoundException() }
        val hikers = hikerRepository.findAllById(bookingCreationRequest.hikerIds)

        validate(bookingCreationRequest, trail, hikers)

        return with(BookingEntity(trail = trail, hikers = hikers, date = bookingCreationRequest.date)) {
            bookingRepository.save(this)
                .also { logger.info { "A new booking was created: $this ." } }
        }
    }

    fun getBookings(pageable: Pageable): Page<BookingEntity> {
        return bookingRepository.findAll(pageable)
    }

    private fun validate(request: BookingCreationRequest, trail: TrailEntity, hikers: List<HikerEntity>) {
        val errors = ArrayList<String>()

        if (LocalDateTime.now().isAfter(LocalDateTime.of(request.date, trail.startAt))) {
            errors.add("It's not possible to finalize the booking because the trail ${trail.name} of today has already started at ${trail.startAt}.")
        }

        if (hikers.size != request.hikerIds.size) {
            val hikerIdsNotFound = request.hikerIds.minus(hikers.map { hiker -> hiker.id })
            errors.add("The hikers $hikerIdsNotFound were not found.")
        }

        hikers.filter { hiker -> (hiker.age() in trail.minimumAge..trail.maximumAge).not() }
            .forEach { hiker -> errors.add("The hiker ${hiker.id} did not met the minimum requirement of participation (age between ${trail.minimumAge} and ${trail.maximumAge})") }

        val errorMessage =
            "There was an error while processing the booking creation request, the validation failed: $errors."
        require(errors.isEmpty()) { errorMessage }
            .also { logger.error { errorMessage } }
    }

    fun retrieveBookingById(id: UUID) = bookingRepository.findById(id)
        .also { it.ifPresent { booking -> logger.info { "Booking found $booking" } } }

    fun cancelBooking(id: UUID) = bookingRepository.cancelBooking(id)
}
