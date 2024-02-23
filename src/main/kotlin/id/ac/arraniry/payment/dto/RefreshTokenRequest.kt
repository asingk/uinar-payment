package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "Token must not be blank")
    val token: String
)
