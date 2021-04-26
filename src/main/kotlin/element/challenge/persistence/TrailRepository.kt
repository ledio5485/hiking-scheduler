package element.challenge.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TrailRepository : JpaRepository<TrailEntity, UUID>