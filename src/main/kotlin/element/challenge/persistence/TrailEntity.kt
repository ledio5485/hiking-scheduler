package element.challenge.persistence

import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.time.LocalTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Entity
@Table(name = "TRAIL")
data class TrailEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @NotBlank
    val name: String,

    @NotNull
    val startAt: LocalTime,

    @NotNull
    val endAt: LocalTime,

    @NotNull
    @Positive
    val minimumAge: Short,

    @NotNull
    @Positive
    val maximumAge: Short,

    @NotNull
    @Positive
    val unitPrice: BigDecimal
)
