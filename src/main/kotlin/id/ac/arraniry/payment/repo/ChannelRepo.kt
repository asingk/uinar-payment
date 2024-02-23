package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Channel
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ChannelRepo: CrudRepository<Channel, UUID> {
    fun findByIdAndDisabled(id: UUID, disabled: Boolean): Channel?
}