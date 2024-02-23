package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Item
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ItemRepo: CrudRepository<Item, UUID> {
    fun findByIdAndDisabled(id: UUID, disabled: Boolean): Item?
}