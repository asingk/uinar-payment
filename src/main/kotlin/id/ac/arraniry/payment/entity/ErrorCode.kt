package id.ac.arraniry.payment.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class ErrorCode(
    @Id
    val code: Int,
) {
    lateinit var description: String
}
