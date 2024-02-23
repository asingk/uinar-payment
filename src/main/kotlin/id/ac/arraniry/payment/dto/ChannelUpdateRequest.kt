package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ChannelUpdateRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String,
    @field:NotNull(message = "disabled must not be null")
    val disabled: Boolean,
)
