package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepo : CrudRepository<Customer, String> {
    fun findByIdAndDisabled(id: String, disabled: Boolean): Customer?
}