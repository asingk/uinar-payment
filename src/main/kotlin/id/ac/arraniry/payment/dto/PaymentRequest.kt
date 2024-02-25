package id.ac.arraniry.payment.dto

import jakarta.validation.constraints.NotNull
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

data class PaymentRequest(
    @field:NotNull
    val amount: BigInteger,
    val channelId: UUID? = null,
    val bankTransactionId: String? = null,
    val bankTerminal: String? = null,
    val bankTransactionDate: Date? = null,
)
