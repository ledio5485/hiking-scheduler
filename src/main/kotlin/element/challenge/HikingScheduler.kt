package element.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HikingScheduler

fun main(args: Array<String>) {
	runApplication<HikingScheduler>(*args)
}
