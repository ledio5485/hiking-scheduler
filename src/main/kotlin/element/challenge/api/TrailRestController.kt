package element.challenge.api

import element.challenge.persistence.TrailRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TrailRestController(private val trailRepository: TrailRepository) : TrailResource {

    override fun retrieveTrails(): ResponseEntity<Collection<TrailDTO>> =
        with(trailRepository.findAll().map { it.toDTO() }) {
            ResponseEntity.ok(this)
        }

    override fun retrieveTrailById(id: UUID) =
        with(trailRepository.findById(id).map { it.toDTO() }) {
            ResponseEntity.of(this)
        }
}