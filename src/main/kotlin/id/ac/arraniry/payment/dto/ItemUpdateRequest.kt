package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class ItemUpdateRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String,
    @field:NotNull
    val disabled: Boolean,
    @field:NotNull
    val categoryId: UUID
)
