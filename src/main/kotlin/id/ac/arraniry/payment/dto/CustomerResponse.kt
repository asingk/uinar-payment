package id.ac.arraniry.payment.dto

import java.util.*

data class CustomerResponse(
    val id: String,
    val name: String,
    val address: String?,
    val phone: String?,
    val email: String?,
    val disabled: Boolean,
    val department: String?,
    val categoryId: UUID,
    val categoryName: String
)
