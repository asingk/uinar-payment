package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.CustomerCategory
import id.ac.arraniry.payment.repo.CustomerCatRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerCatService(
    private val customerCatRepo: CustomerCatRepo
) {
    fun findById(id: UUID): CustomerCategory? = customerCatRepo.findByIdOrNull(id)
    fun findAll(): List<CustomerCategory> = customerCatRepo.findAll().toList()
    fun save(customerCategory: CustomerCategory): CustomerCategory = customerCatRepo.save(customerCategory)
    fun delete(id: UUID) = customerCatRepo.deleteById(id)
    fun findByIdAndDisabled(id: UUID, disabled: Boolean): CustomerCategory? = customerCatRepo.findByIdAndDisabled(id, disabled)
}