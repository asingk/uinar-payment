package id.ac.arraniry.payment.dto

import java.math.BigInteger
import java.time.LocalDateTime
import java.util.UUID

data class InvoiceResponse(
    val id: String,
    val amount: BigInteger,
    val expiredDate: LocalDateTime,
    val disabled: Boolean,
    val itemId: UUID,
    val itemName: String,
    val customerId: String,
    val customerName: String,
    val paymentStatusCode: Int,
    val paymentStatusDesc: String,
    val isAvailable: Boolean,
)
