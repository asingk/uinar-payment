package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ConsumerUpdateRequest(
    @field:NotBlank(message = "Name must not be blank")
    val username: String,
    @field:NotBlank(message = "password must not be blank")
    val password: String,
    @field:NotBlank(message = "role must not be blank")
    val role: String,
    @field:NotNull(message = "disabled must not be null")
    val disabled: Boolean
)
