package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.*

data class CustUpdateRequest(
    @field:NotBlank
    val name: String,
    @field:Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "must not be empty")
    val address: String?,
    @field:Pattern(regexp = "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}\$",
        message = "must be a well-formed phone number")
    val phone: String?,
    @field:Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "must not be empty")
    @field:Email
    val email: String?,
    @field:Pattern(regexp = "\\A(?!\\s*\\Z).+", message = "must not be empty")
    val department: String?,
    @field:NotNull
    val categoryId: UUID,
    @field:NotNull
    val disabled: Boolean,
)
