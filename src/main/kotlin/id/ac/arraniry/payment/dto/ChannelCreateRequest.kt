package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank

data class ChannelCreateRequest(
    @field:NotBlank
    val name: String,
)
