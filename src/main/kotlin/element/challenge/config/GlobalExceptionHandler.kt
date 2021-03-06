package element.challenge.config

import brave.Tracer
import element.challenge.config.ErrorResponseEntity.Companion.badRequest
import element.challenge.config.ErrorResponseEntity.Companion.notFound
import element.challenge.config.ErrorResponseEntity.Companion.serverError
import element.challenge.logic.TrailNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler(private val tracer: Tracer) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    fun handleBadRequestException(ex: Exception, request: WebRequest): ErrorResponseEntity {
        return badRequest(ex.message, requestId())
    }

    @ExceptionHandler(value = [TrailNotFoundException::class])
    fun handleNotFoundException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return notFound(ex.message, requestId())
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return serverError(ex.message, requestId())
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val errors = ex.bindingResult.allErrors.map { objectError ->
            if (objectError is FieldError) {
                "'${objectError.field}:${objectError.rejectedValue}' ${objectError.defaultMessage}"
            } else {
                objectError.defaultMessage
            }
        }
        return handleExceptionInternal(ex, ErrorResponse(status, errors.joinToString(), requestId = requestId()), headers, status, request)
    }

    private fun requestId() = tracer.currentSpan().context().traceIdString()
}