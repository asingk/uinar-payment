package id.ac.arraniry.payment.dto

import java.util.*

data class ItemResponse(
    val id: UUID,
    val name: String,
    val disabled: Boolean,
    val categoryId: UUID,
    val categoryName: String,
)
