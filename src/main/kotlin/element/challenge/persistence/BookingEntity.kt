package element.challenge.persistence

import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Where
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "BOOKING")
@DynamicUpdate
@Where(clause = "is_cancelled=false")
data class BookingEntity(

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null,

    @OneToOne
    val trail: TrailEntity,

    @ManyToMany
    @JoinTable(
        name = "BOOKING_HIKER",
        joinColumns = [JoinColumn(name = "BOOKING_ID")],
        inverseJoinColumns = [JoinColumn(name = "HIKER_ID")]
    )
    val hikers: List<HikerEntity>,

    val date: LocalDate = LocalDate.now(),

    var isCancelled: Boolean = false
)
