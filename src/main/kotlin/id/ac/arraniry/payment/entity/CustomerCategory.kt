package id.ac.arraniry.payment.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.LocalDateTime
import java.util.*

@Entity
data class CustomerCategory(
    @Id
    val id: UUID,
    var name: String,
    var disabled: Boolean,
    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: Consumer,
    val createdDate: LocalDateTime,
    @OneToMany(mappedBy = "category")
    val customer: List<Customer> = mutableListOf()
) {
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: Consumer? = null
    var lastModifiedDate: LocalDateTime? = null
}