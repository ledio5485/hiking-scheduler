package element.challenge.api

import element.challenge.api.TrailResource.Companion.TRAILS_URI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

@RequestMapping(TRAILS_URI)
interface TrailResource {

    companion object {
        const val TRAILS_URI = "/api/trails"
    }

    @GetMapping
    @ResponseBody
    fun retrieveTrails(): ResponseEntity<Collection<TrailDTO>>

    @GetMapping("/{id}")
    @ResponseBody
    fun retrieveTrailById(@PathVariable("id") id: UUID): ResponseEntity<TrailDTO>
}