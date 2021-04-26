package element.challenge.persistence

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDate
import java.time.Period
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past

@Entity
@Table(name = "HIKER")
data class HikerEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @NotBlank
    val name: String,

    @NotBlank
    val surname: String,

    @NotNull
    @Past
    val dateOfBirth: LocalDate,

    @NotBlank
    val phoneNumber: String,

    @Email
    val email: String

    //@ManyToMany(mappedBy = "hikers")
    //val booking: Set<BookingEntity> = emptySet()
) {
    fun age() = Period.between(dateOfBirth, LocalDate.now()).years
}
