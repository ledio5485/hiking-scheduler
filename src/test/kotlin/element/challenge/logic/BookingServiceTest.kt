package element.challenge.logic

import element.challenge.api.BookingCreationRequest
import element.challenge.persistence.BookingRepository
import element.challenge.persistence.HikerEntity
import element.challenge.persistence.HikerRepository
import element.challenge.persistence.TrailEntity
import element.challenge.persistence.TrailRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import org.mockito.Mockito.`when` as When

@ExtendWith(MockitoExtension::class)
internal class BookingServiceTest(
    @Mock private val bookingRepository: BookingRepository,
    @Mock private val hikerRepository: HikerRepository,
    @Mock private val trailRepository: TrailRepository
) {

    @InjectMocks
    private lateinit var sut: BookingService

    @Test
    internal fun `should throw TrailNotFoundException when trail is not found`() {
        val trailId = UUID.randomUUID()
        val hikerId = UUID.randomUUID()
        When(trailRepository.findById(trailId)).thenReturn(Optional.empty())
        val hiker =
            HikerEntity(hikerId, "name", "surname", LocalDate.of(2000, 12, 12), "+1 234 567890", "name@surname.element")
        When(hikerRepository.findById(hikerId)).thenReturn(Optional.of(hiker))

        assertThrows<TrailNotFoundException> {
            val bookingCreationRequest = BookingCreationRequest(trailId, setOf(hikerId))
            sut.createBooking(bookingCreationRequest)
        }.also { exception ->
            assertThat(exception.message).contains("Trail not found.")
        }
    }

    @Test
    internal fun `should throw IllegalArgumentException when trying to book a trail that has already started`() {
        val trailId = UUID.randomUUID()
        val hikerId = UUID.randomUUID()
        val trail = TrailEntity(trailId, "T1", LocalTime.now(), LocalTime.now().plusHours(1), 10, 20, BigDecimal.ONE)
        When(trailRepository.findById(trailId)).thenReturn(Optional.of(trail))
        val hiker = HikerEntity(
            hikerId,
            "name",
            "surname",
            LocalDate.now().minusYears(15),
            "+1 234 567890",
            "name@surname.element"
        )
        When(hikerRepository.findAllById(listOf(hikerId))).thenReturn(listOf(hiker))

        assertThrows<IllegalArgumentException> {
            val bookingCreationRequest = BookingCreationRequest(trailId, setOf(hikerId))
            sut.createBooking(bookingCreationRequest)
        }.also { exception ->
            assertThat(exception.message).contains("It's not possible to finalize the booking because the trail ${trail.name} of today has already started at ${trail.startAt}.")
        }
    }

    @Test
    internal fun `should throw IllegalArgumentException when at least one hiker is not found`() {
        val trailId = UUID.randomUUID()
        val hikerId1 = UUID.randomUUID()
        val hikerId2 = UUID.randomUUID()
        val trail = TrailEntity(trailId, "T1", LocalTime.now(), LocalTime.now().plusHours(1), 10, 20, BigDecimal.ONE)
        When(trailRepository.findById(trailId)).thenReturn(Optional.of(trail))
        val hiker = HikerEntity(
            hikerId1,
            "name",
            "surname",
            LocalDate.now().minusYears(((trail.minimumAge + trail.maximumAge)/2).toLong()),
            "+1 234 567890",
            "name@surname.element"
        )
        When(hikerRepository.findAllById(setOf(hikerId1, hikerId2))).thenReturn(listOf(hiker))

        assertThrows<IllegalArgumentException> {
            val bookingCreationRequest =
                BookingCreationRequest(trailId, setOf(hikerId1, hikerId2), LocalDate.now().plusDays(1))
            sut.createBooking(bookingCreationRequest)
        }.also { exception ->
            assertThat(exception.message).contains("The hikers ${listOf(hikerId2)} were not found.")
        }
    }

    @Test
    internal fun `should throw IllegalArgumentException when at least one hiker's age is not in trail age range`() {
        val trailId = UUID.randomUUID()
        val hikerId1 = UUID.randomUUID()
        val hikerId2 = UUID.randomUUID()
        val trail = TrailEntity(trailId, "T1", LocalTime.now(), LocalTime.now().plusHours(1), 10, 20, BigDecimal.ONE)
        When(trailRepository.findById(trailId)).thenReturn(Optional.of(trail))
        val hiker1 = HikerEntity(
            hikerId1,
            "name",
            "surname",
            LocalDate.now().minusYears(15),
            "+1 234 567890",
            "name@surname.element"
        )
        val hiker2 = HikerEntity(
            hikerId2,
            "name",
            "surname",
            LocalDate.now().minusYears(30),
            "+1 234 567890",
            "name@surname.element"
        )
        When(hikerRepository.findAllById(setOf(hikerId1, hikerId2))).thenReturn(listOf(hiker1, hiker2))

        assertThrows<IllegalArgumentException> {
            val bookingCreationRequest =
                BookingCreationRequest(trailId, setOf(hikerId1, hikerId2), LocalDate.now().plusDays(1))
            sut.createBooking(bookingCreationRequest)
        }.also { exception ->
            assertThat(exception.message).contains("The hiker ${hiker2.id} did not met the minimum requirement of participation (age between ${trail.minimumAge} and ${trail.maximumAge})")
        }
    }
}