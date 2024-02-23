package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.Invoice
import org.springframework.data.repository.CrudRepository

interface InvoiceRepo: CrudRepository<Invoice, String> {
    fun findByIdAndDisabled(id: String, disabled: Boolean): Invoice?
}