package id.ac.arraniry.payment.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class ItemCategory(
    @Id
    val id: UUID,
    var name: String,
    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: Consumer,
    val createdDate: LocalDateTime,
    @OneToMany(mappedBy = "category")
    val item: List<Item> = mutableListOf()
) {
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: Consumer? = null
    var lastModifiedDate: LocalDateTime? = null
}