package element.challenge.api

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

data class TrailDTO(
    val id: UUID,
    val name: String,
    val startAt: LocalTime,
    val endAt: LocalTime,
    val minimumAge: Short,
    val maximumAge: Short,
    val unitPrice: BigDecimal
)

data class HikerDTO(
    val id: UUID?,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val surname: String,

    @field:Past
    val dateOfBirth: LocalDate,

    @NotBlank
    val phoneNumber: String,

    @Email
    val email: String
)

data class BookingCreationRequest(
    @field:NotNull
    val trailId: UUID,

    @field:NotNull
    @field:NotEmpty
    val hikerIds: Set<UUID>,

    @field:NotNull
    @field:Future
    val date: LocalDate = LocalDate.now()
)

data class BookingDTO(
    val id: UUID,
    val trail: TrailDTO,
    val hikers: Set<HikerDTO>,
    val date: LocalDate,
    val price: BigDecimal = trail.unitPrice.multiply(BigDecimal(hikers.size))
)
