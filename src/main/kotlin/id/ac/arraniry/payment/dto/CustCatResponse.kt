package id.ac.arraniry.payment.dto

import java.util.*

data class CustCatResponse(
    val id: UUID,
    var name: String,
    var disabled: Boolean,
)
