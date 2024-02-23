package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Item
import id.ac.arraniry.payment.repo.ItemRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ItemService(
    private val itemRepo: ItemRepo
) {
    fun findById(id: UUID): Item? = itemRepo.findByIdOrNull(id)
    fun save(item: Item): Item = itemRepo.save(item)
    fun delete(id: UUID) = itemRepo.deleteById(id)
    fun findByIdAndDisabled(id: UUID, disabled: Boolean): Item? = itemRepo.findByIdAndDisabled(id, disabled)
}