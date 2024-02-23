package id.ac.arraniry.payment.dto

import id.ac.arraniry.payment.entity.Role
import java.util.UUID

data class ConsumerResponse(
    val id: UUID,
    val username: String,
    val disabled: Boolean,
    val role: String,
)
