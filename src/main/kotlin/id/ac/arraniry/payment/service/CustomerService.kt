package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Customer
import id.ac.arraniry.payment.repo.CustomerRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepo: CustomerRepo
) {
    fun findById(id: String): Customer? = customerRepo.findByIdOrNull(id)
    fun save(customer: Customer): Customer = customerRepo.save(customer)
    fun delete(id: String) = customerRepo.deleteById(id)
    fun findByIdAndDisabled(id: String, disabled: Boolean): Customer? = customerRepo.findByIdAndDisabled(id, disabled)
    fun saveAll(customer: List<Customer>): MutableIterable<Customer> = customerRepo.saveAll(customer)
}