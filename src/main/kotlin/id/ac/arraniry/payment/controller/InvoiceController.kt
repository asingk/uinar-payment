package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.GlobalConstants
import id.ac.arraniry.payment.dto.*
import id.ac.arraniry.payment.entity.*
import id.ac.arraniry.payment.service.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.math.BigInteger
import java.security.Principal
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/invoices")
class InvoiceController(
    private val invoiceService: InvoiceService,
    private val consumerService: ConsumerService,
    private val customerService: CustomerService,
    private val itemService: ItemService,
): BaseController() {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): InvoiceResponse =
        invoiceService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "invoice does not found")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) {
        val exist = invoiceService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "invoice does not found")
        if (exist.paymentStatus.code > GlobalConstants.PAYMENT_STATUS_BELUM_BAYAR)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        invoiceService.delete(id)
    }

    @GetMapping("/{id}/payments")
    fun getPayments(@PathVariable id: String): List<PaymentResponse> =
        invoiceService.findById(id)?.toPaymentResponseList() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "invoice does not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bulk")
    fun createInvoicesBulk(@RequestBody invoiceBulkRequest: List<InvoiceBulkRequest>, principal: Principal): List<InvoiceResponse> {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val reqList: MutableList<Invoice> = mutableListOf()
        for (req in invoiceBulkRequest) {
            val customer = customerService.findByIdAndDisabled(req.customerId, false)
            val item = itemService.findByIdAndDisabled(req.itemId, false)
            if (null != customer && null != item && req.amount >= BigInteger.ZERO && req.expirationDate.isAfter(LocalDateTime.now())) {
                reqList.add(req.toModel(consumer, customer, item))
            }
        }

        return invoiceService.saveAll(reqList).map { it.toResponse() }
    }

    private fun Invoice.toResponse(): InvoiceResponse =
        InvoiceResponse(
            id = this.id,
            amount = this.amount,
            disabled = false,
            expiredDate = this.expiredDate,
            itemId = this.item.id,
            itemName = this.item.name,
            customerId = this.customer.id,
            customerName = this.customer.name,
            paymentStatusCode = this.paymentStatus.code,
            paymentStatusDesc = this.paymentStatus.description,
            isAvailable = !disabled && LocalDateTime.now().isBefore(this.expiredDate) && this.paymentStatus.code == GlobalConstants.PAYMENT_STATUS_BELUM_BAYAR
        )

    private fun Invoice.toPaymentResponseList(): List<PaymentResponse> =
        this.payment.map {
            PaymentResponse(
                id = it.id,
                amount = it.amount,
                bankTerminal = it.bankTerminal,
                bankTransactionId = it.bankTransactionId,
                bankTransactionDate = it.bankTransactionDate,
                paymentStatusCode = it.paymentStatus.code,
                paymentStatusDesc = it.paymentStatus.description,
                invoiceId = it.invoice.id,
                channelId = it.channel?.id,
                channelName = it.channel?.name,
                errorCode = it.errorCode?.code,
                errorDesc = it.errorCode?.description,
            )
        }

    private fun InvoiceBulkRequest.toModel(consumer: Consumer, customer: Customer, item: Item): Invoice =
        Invoice(
            id = generateRandomInvoice(customer),
            disabled = false,
            amount = this.amount,
            expiredDate = this.expirationDate,
            item = item,
            customer = customer,
            createdBy = consumer,
            createdDate = LocalDateTime.now(),
            paymentStatus = if (this.amount == BigInteger.ZERO) PaymentStatus(GlobalConstants.PAYMENT_STATUS_SUDAH_BAYAR)
                                else PaymentStatus(GlobalConstants.PAYMENT_STATUS_BELUM_BAYAR)
        )

}