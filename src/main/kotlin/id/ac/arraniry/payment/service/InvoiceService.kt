package id.ac.arraniry.payment.service

import id.ac.arraniry.payment.entity.Invoice
import id.ac.arraniry.payment.repo.InvoiceRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class InvoiceService(
    private val invoiceRepo: InvoiceRepo,
) {
    fun findById(id: String): Invoice? = invoiceRepo.findByIdOrNull(id)
    fun save(invoice: Invoice): Invoice = invoiceRepo.save(invoice)
    fun delete(id: String) = invoiceRepo.deleteById(id)
    fun findByIdAndDisabled(id: String, disabled: Boolean): Invoice? = invoiceRepo.findByIdAndDisabled(id, disabled)
}