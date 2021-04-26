package element.challenge.logic

class TrailNotFoundException(override val message: String? = "Trail not found.") : RuntimeException(message)