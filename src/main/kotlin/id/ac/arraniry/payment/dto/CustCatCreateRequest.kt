package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank

data class CustCatCreateRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String
)