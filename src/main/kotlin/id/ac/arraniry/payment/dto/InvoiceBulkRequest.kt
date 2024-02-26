package id.ac.arraniry.payment.dto

import java.math.BigInteger
import java.time.LocalDateTime
import java.util.UUID

data class InvoiceBulkRequest(
    val customerId: String,
    val amount: BigInteger,
    val expirationDate: LocalDateTime,
    val itemId: UUID,
)