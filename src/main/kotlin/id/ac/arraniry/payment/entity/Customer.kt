package id.ac.arraniry.payment.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Customer(
    @Id
    val id: String,
    var name: String,
    var address: String?,
    var phone: String?,
    var email: String?,
    var disabled: Boolean = false,
    var department: String?,
    @ManyToOne
    @JoinColumn(name = "created_by" , nullable = false)
    val createdBy: Consumer,
    val createdDate: LocalDateTime,
    @ManyToOne
    @JoinColumn(name = "cust_cat_id", nullable = false)
    var category: CustomerCategory,
    @OneToMany(mappedBy = "customer")
    val invoice: List<Invoice> = mutableListOf()
) {
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: Consumer? = null
    var lastModifiedDate: LocalDateTime? = null
}