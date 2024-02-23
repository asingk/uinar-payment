package id.ac.arraniry.payment.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Consumer(
    @Id
    val id: UUID,
    var username: String,
    var password: String,
    var disabled: Boolean = false,
    @ManyToOne
    @JoinColumn(name = "role_code")
    var role: Role,
    val createdBy: UUID,
    val createdDate: LocalDateTime,
) {
    var lastModifiedBy: UUID? = null
    var lastModifiedDate: LocalDateTime? = null
}