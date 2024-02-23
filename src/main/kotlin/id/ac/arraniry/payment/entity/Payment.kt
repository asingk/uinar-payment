package id.ac.arraniry.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Payment(
    @Id
    val id: UUID,
    val amount: BigInteger,
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    val invoice: Invoice,
    @ManyToOne
    @JoinColumn(name = "payment_status_code")
    val paymentStatus: PaymentStatus,
    @ManyToOne
    @JoinColumn(name = "channel_id")
    val channel: Channel? = null,
    @Column(name = "bank_trx_id")
    val bankTransactionId: String? = null,
    val bankTerminal: String? = null,
    @Column(name = "bank_trx_date")
    val bankTransactionDate: LocalDateTime? = null,
    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: Consumer,
    val createdDate: LocalDateTime,
    @ManyToOne
    @JoinColumn(name = "error_code")
    val errorCode: ErrorCode? = null
)
