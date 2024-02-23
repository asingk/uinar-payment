package id.ac.arraniry.payment.dto

import java.util.UUID

data class ChannelResponse(
    val id: UUID,
    val name: String,
    val disabled: Boolean,
)
