package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.CustomerCategory
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CustomerCatRepo: CrudRepository<CustomerCategory, UUID> {
    fun findByIdAndDisabled(id: UUID, disabled: Boolean): CustomerCategory?
}