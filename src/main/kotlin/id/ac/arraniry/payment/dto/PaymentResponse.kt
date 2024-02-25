package id.ac.arraniry.payment.dto

import java.math.BigInteger
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class PaymentResponse(
    val id: UUID,
    val amount: BigInteger,
    val invoiceId: String,
    val paymentStatusCode: Int,
    val paymentStatusDesc: String,
    val channelId: UUID?,
    val channelName: String?,
    val bankTransactionId: String?,
    val bankTerminal: String?,
    val bankTransactionDate: Date?,
    val errorCode: Int?,
    val errorDesc: String?,
)
