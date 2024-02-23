package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.ItemCategory
import id.ac.arraniry.payment.repo.ItemCatRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ItemCatService(
    private val itemCatRepo: ItemCatRepo
) {
    fun findById(id: UUID): ItemCategory? = itemCatRepo.findByIdOrNull(id)
    fun findAll(): List<ItemCategory> = itemCatRepo.findAll().toList()
    fun save(itemCategory: ItemCategory): ItemCategory = itemCatRepo.save(itemCategory)
    fun delete(id: UUID) = itemCatRepo.deleteById(id)
}