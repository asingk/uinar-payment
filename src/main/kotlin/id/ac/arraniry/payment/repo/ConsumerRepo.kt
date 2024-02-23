package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Consumer
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ConsumerRepo : CrudRepository<Consumer, UUID> {
    fun findByUsernameAndDisabled(username: String, disabled: Boolean): Consumer?
}