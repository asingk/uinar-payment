package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Payment
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PaymentRepo: CrudRepository<Payment, UUID>