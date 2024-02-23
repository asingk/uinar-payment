package id.ac.arraniry.payment.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.math.BigInteger
import java.time.LocalDateTime

@Entity
data class Invoice(
    @Id
    val id: String,
    var amount: BigInteger,
    var expiredDate: LocalDateTime,
    var disabled: Boolean,
    @ManyToOne
    @JoinColumn(name = "item_id")
    var item: Item,
    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: Customer,
    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: Consumer,
    val createdDate: LocalDateTime,
    @ManyToOne
    @JoinColumn(name = "payment_status_code")
    var paymentStatus: PaymentStatus,
    @OneToMany(mappedBy = "invoice")
    val payment: List<Payment> = mutableListOf()
) {
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: Consumer? = null
    var lastModifiedDate: LocalDateTime? = null
}