package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank

data class ConsumerCreateRequest(
    @field:NotBlank(message = "Name must not be blank")
    val username: String,
    @field:NotBlank(message = "password must not be blank")
    val password: String,
    @field:NotBlank(message = "role must not be blank")
    val role: String
)
