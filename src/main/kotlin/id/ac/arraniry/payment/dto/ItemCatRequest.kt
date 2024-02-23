package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank

data class ItemCatRequest (
    @field:NotBlank(message = "Name must not be blank")
    val name: String
)