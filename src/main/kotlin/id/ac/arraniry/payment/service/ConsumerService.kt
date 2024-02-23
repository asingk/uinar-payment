package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.repo.ConsumerRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ConsumerService(
    private val consumerRepo: ConsumerRepo
) {
    fun findById(id: UUID): Consumer? = consumerRepo.findByIdOrNull(id)
    fun findAll(): List<Consumer> = consumerRepo.findAll().toList()
    fun save(consumer: Consumer): Consumer = consumerRepo.save(consumer)
    fun delete(id: UUID) = consumerRepo.deleteById(id)
    fun findByUsernameAndDisabled(username: String, disabled: Boolean): Consumer? = consumerRepo.findByUsernameAndDisabled(username, disabled)
}