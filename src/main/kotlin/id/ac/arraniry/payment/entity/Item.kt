package id.ac.arraniry.payment.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Item(
    @Id
    val id: UUID,
    var name: String,
    var disabled: Boolean = false,
    @ManyToOne
    @JoinColumn(name = "item_cat_id")
    var category: ItemCategory,
    @ManyToOne
    @JoinColumn(name = "created_by")
    val createdBy: Consumer,
    val createdDate: LocalDateTime,
) {
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    var lastModifiedBy: Consumer? = null
    var lastModifiedDate: LocalDateTime? = null
}
