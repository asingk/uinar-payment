package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.UUID

data class InvoiceUpdateRequest(
    @field:NotNull
    @field:Min(0)
    val amount: BigInteger,
    @field:Future
    @field:NotNull
    val expirationDate: LocalDateTime,
    @field:NotNull
    val itemId: UUID,
    @field:NotNull
    val disabled: Boolean,
)