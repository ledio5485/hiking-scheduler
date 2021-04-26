package element.challenge.api

import element.challenge.api.HikerResource.Companion.HIKERS_URI
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*
import javax.validation.Valid

@RequestMapping(HIKERS_URI)
interface HikerResource {
    companion object {
        const val HIKERS_URI = "/api/hikers"
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    fun createHiker(@RequestBody @Valid newHiker: HikerDTO): ResponseEntity<HikerDTO>

    @GetMapping("/{id}")
    @ResponseBody
    fun retrieveHikerById(@PathVariable("id") id: UUID): ResponseEntity<HikerDTO>
}