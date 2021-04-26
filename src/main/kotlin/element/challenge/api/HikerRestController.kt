package element.challenge.api

import element.challenge.persistence.HikerRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class HikerRestController(private val hikerRepository: HikerRepository) : HikerResource {

    override fun createHiker(newHiker: HikerDTO) =
        with(hikerRepository.save(newHiker.toEntity())) {
            ResponseEntity.status(HttpStatus.CREATED).body(toDTO())
        }

    override fun retrieveHikerById(id: UUID) =
        with(hikerRepository.findById(id).map { it.toDTO() }) {
            ResponseEntity.of(this)
        }
}