package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotNull
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.UUID

data class ReversalRequest(
    @field:NotNull
    val amount: BigInteger,
)
