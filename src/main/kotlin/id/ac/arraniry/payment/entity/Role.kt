package id.ac.arraniry.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Role(
    @Id
    val code: String,
    val description: String
)