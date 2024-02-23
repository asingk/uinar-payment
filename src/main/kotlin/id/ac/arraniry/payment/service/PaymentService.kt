package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Payment
import id.ac.arraniry.payment.repo.PaymentRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PaymentService(
    private val paymentRepo: PaymentRepo,
) {
    fun findById(id: UUID): Payment? = paymentRepo.findByIdOrNull(id)
    fun save(payment: Payment): Payment {
        return paymentRepo.save(payment)
    }
}