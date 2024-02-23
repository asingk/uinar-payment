package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.GlobalConstants
import id.ac.arraniry.payment.dto.*
import id.ac.arraniry.payment.entity.*
import id.ac.arraniry.payment.service.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.security.Principal
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService,
    private val consumerService: ConsumerService,
    private val customerCatService: CustomerCatService,
    private val itemService: ItemService,
    private val invoiceService: InvoiceService,
    private val channelService: ChannelService,
    private val paymentService: PaymentService,
) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): CustomerResponse =
        customerService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer does not found")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    fun create(@Valid @RequestBody request: CustCreateRequest, principal: Principal) {
        customerService.findById(request.id)?.let { throw ResponseStatusException(HttpStatus.CONFLICT, "customer has already exist") }
        val custCat = customerCatService.findByIdAndDisabled(request.categoryId, false)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer category does not found")
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (request.categoryId == GlobalConstants.MAHASISWA_CAT_ID && request.id.length > 9)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        customerService.save(request.toModel(consumer, custCat))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @Valid @RequestBody request: CustUpdateRequest, principal: Principal) {
        val existed = customerService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer does not found")
        val custCat = customerCatService.findByIdAndDisabled(request.categoryId, false)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer category does not found")
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (request.categoryId == GlobalConstants.MAHASISWA_CAT_ID && id.length > 9)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        customerService.save(request.toModel(existed, consumer, custCat))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = customerService.delete(id)

    @GetMapping("/{id}/invoices")
    fun getInvoice(@PathVariable id: String): List<InvoiceResponse> =
        customerService.findById(id)?.toInvoicelist() ?:throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer does not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/invoices")
    fun create(@PathVariable id: String, @Valid @RequestBody invoiceCreateRequest: InvoiceCreateRequest, principal: Principal): CreateStringResponse {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val customer = customerService.findByIdAndDisabled(id, false)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer does not found")
        val item = itemService.findByIdAndDisabled(invoiceCreateRequest.itemId, false)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item does not found")
        return CreateStringResponse(invoiceService.save(invoiceCreateRequest.toModel(consumer, customer, item)).id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}/invoices/{invoiceId}")
    fun update(@PathVariable id: String, @Valid @RequestBody invoiceUpdateRequest: InvoiceUpdateRequest, principal: Principal,
               @PathVariable invoiceId: String
    ) {
        val exist = invoiceService.findById(invoiceId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "invoice does not found")
        if (exist.paymentStatus.code > 0)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val customer = customerService.findByIdAndDisabled(id, false)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer does not found")
        val item = itemService.findByIdAndDisabled(invoiceUpdateRequest.itemId, false)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item does not found")
        invoiceService.save(invoiceUpdateRequest.toModel(consumer, customer, item, exist))
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/invoices/{invoiceId}/payments")
    fun pay(@PathVariable id: String, principal: Principal, @Valid @RequestBody request: PaymentRequest,
            @PathVariable invoiceId: String
    ): CreateUUIDResponse {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val invoice = invoiceService.findByIdAndDisabled(invoiceId, false) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "invoice does not found")
        if (invoice.paymentStatus.code > GlobalConstants.PAYMENT_STATUS_BELUM_BAYAR)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (invoice.customer.id != id)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (invoice.amount != request.amount)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (!LocalDateTime.now().isBefore(invoice.expiredDate))
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        var channel: Channel? = null
        if(request.channelId != null)
            channel = channelService.findByIdAndDisabled(request.channelId, false) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "channel does not found")
        invoice.paymentStatus = PaymentStatus(GlobalConstants.PAYMENT_STATUS_SUDAH_BAYAR)
        invoice.lastModifiedBy = consumer
        invoice.lastModifiedDate = LocalDateTime.now()
        return CreateUUIDResponse(paymentService.save(request.toModel(channel, invoice)).id)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/invoices/{invoiceId}/payments/reversal")
    fun reversal(@PathVariable id: String, @PathVariable invoiceId: String, principal: Principal,
                 @Valid @RequestBody request: ReversalRequest): CreateUUIDResponse {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val invoice = invoiceService.findByIdAndDisabled(invoiceId, false) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "invoice does not found")
        if (invoice.paymentStatus.code != GlobalConstants.PAYMENT_STATUS_SUDAH_BAYAR)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (invoice.customer.id != id)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        if (invoice.amount != request.amount)
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        invoice.paymentStatus = PaymentStatus(GlobalConstants.PAYMENT_STATUS_REVERSAL)
        invoice.lastModifiedBy = consumer
        invoice.lastModifiedDate = LocalDateTime.now()
        return CreateUUIDResponse(paymentService.save(request.toModel(invoice)).id)
    }

    private fun Customer.toResponse(): CustomerResponse =
        CustomerResponse(
            id = this.id,
            name = this.name,
            address = this.address,
            phone = this.phone,
            email = this.email,
            disabled = this.disabled,
            department = this.department,
            categoryId = this.category.id,
            categoryName = this.category.name
        )

    private fun CustCreateRequest.toModel(consumer: Consumer, customerCategory: CustomerCategory): Customer =
        Customer(
            id = this.id,
            name = this.name,
            address = this.address,
            phone = this.phone,
            email = this.email,
            disabled = false,
            department = this.department,
            category = customerCategory,
            createdBy = consumer,
            createdDate = LocalDateTime.now()
        )

    private fun CustUpdateRequest.toModel(customer: Customer, consumer: Consumer, customerCategory: CustomerCategory): Customer {
        customer.name = this.name
        customer.address = this.address
        customer.phone = this.phone
        customer.email = this.email
        customer.disabled = this.disabled
        customer.category = customerCategory
        customer.lastModifiedBy = consumer
        customer.lastModifiedDate = LocalDateTime.now()
        return customer
    }

    private fun InvoiceCreateRequest.toModel(consumer: Consumer, customer: Customer, item: Item): Invoice =
        Invoice(
            id = generateRandomInvoice(customer),
            disabled = false,
            amount = this.amount,
            expiredDate = this.expirationDate,
            item = item,
            customer = customer,
            createdBy = consumer,
            createdDate = LocalDateTime.now(),
            paymentStatus = PaymentStatus(GlobalConstants.PAYMENT_STATUS_BELUM_BAYAR)
        )

    private fun InvoiceUpdateRequest.toModel(consumer: Consumer, customer: Customer, item: Item, invoice: Invoice): Invoice {
        invoice.disabled = this.disabled
        invoice.amount = this.amount
        invoice.expiredDate = this.expirationDate
        invoice.item = item
        invoice.customer = customer
        invoice.lastModifiedBy = consumer
        invoice.lastModifiedDate = LocalDateTime.now()
        return invoice
    }

    private fun PaymentRequest.toModel(channel: Channel?, invoice: Invoice): Payment =
        Payment(
            id = UUID.randomUUID(),
            amount = this.amount,
            paymentStatus = invoice.paymentStatus,
            createdBy = invoice.lastModifiedBy!!,
            createdDate = invoice.lastModifiedDate!!,
            channel = channel,
            invoice = invoice,
            bankTransactionDate = this.bankTransactionDate,
            bankTransactionId = this.bankTransactionId,
            bankTerminal = this.bankTerminal,
        )

    private fun ReversalRequest.toModel(invoice: Invoice): Payment =
        Payment(
            id = UUID.randomUUID(),
            amount = this.amount,
            paymentStatus = invoice.paymentStatus,
            createdBy = invoice.lastModifiedBy!!,
            createdDate = invoice.lastModifiedDate!!,
            invoice = invoice,
        )

    private fun Customer.toInvoicelist(): List<InvoiceResponse> =
        this.invoice.map {
            InvoiceResponse(
                id = it.id,
                amount = it.amount,
                disabled = it.disabled,
                expiredDate = it.expiredDate,
                customerId = it.customer.id,
                customerName = it.customer.name,
                itemId = it.item.id,
                itemName = it.item.name,
                paymentStatusCode = it.paymentStatus.code,
                paymentStatusDesc = it.paymentStatus.description
            )
        }

    private fun generateRandomInvoice(customer: Customer): String {
        val leftLimit = 48 // numeral '0'
        val rightLimit = 57 // numeral '9'
        val random = Random()
        if (customer.category.id == GlobalConstants.MAHASISWA_CAT_ID) {
            val rand = random.ints(leftLimit, rightLimit + 1)
                .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
                .limit(3)
                .collect(
                    { StringBuilder() },
                    { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }
                ) { obj: StringBuilder, s: StringBuilder? ->
                    obj.append(
                        s
                    )
                }
                .toString()
            return customer.id + rand
        } else {
            return random.ints(leftLimit, rightLimit + 1)
                .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
                .limit(12)
                .collect(
                    { StringBuilder() },
                    { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }
                ) { obj: StringBuilder, s: StringBuilder? ->
                    obj.append(
                        s
                    )
                }
                .toString()
        }

    }

}