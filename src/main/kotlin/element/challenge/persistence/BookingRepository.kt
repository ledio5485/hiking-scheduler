package element.challenge.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

@Repository
interface BookingRepository : JpaRepository<BookingEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE BookingEntity b SET b.isCancelled = TRUE WHERE b.id = :id")
    fun cancelBooking(@Param(value = "id") id: UUID)

}