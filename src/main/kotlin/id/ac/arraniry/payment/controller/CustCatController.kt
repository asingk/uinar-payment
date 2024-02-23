package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.dto.*
import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.entity.CustomerCategory
import id.ac.arraniry.payment.service.ConsumerService
import id.ac.arraniry.payment.service.CustomerCatService
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
import java.util.UUID

@RestController
@RequestMapping("/customer-categories")
class CustCatController(
    private val customerCatService: CustomerCatService,
    private val consumerService: ConsumerService,
) {
    @GetMapping
    fun getAll(): List<CustCatResponse> = customerCatService.findAll().map { it.toResponse() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): CustCatResponse =
        customerCatService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer category does not found")

    @GetMapping("/{id}/customers")
    fun getCustomerById(@PathVariable id: UUID): List<CustomerResponse> =
        customerCatService.findById(id)?.toCustomerResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer category does not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody request: CustCatCreateRequest, principal: Principal): CreateUUIDResponse {
        val createdBy = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        return CreateUUIDResponse(customerCatService.save(request.toModel(createdBy)).id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: CustCatUpdateRequest, principal: Principal) {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val existed = customerCatService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "consumer does not found")
        customerCatService.save(request.toModel(existed, consumer))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:UUID) = customerCatService.delete(id)

    private fun CustomerCategory.toResponse(): CustCatResponse =
        CustCatResponse(
            id = this.id,
            name = this.name,
            disabled = this.disabled
        )

    private fun CustCatCreateRequest.toModel(consumer: Consumer): CustomerCategory =
        CustomerCategory(
            id = UUID.randomUUID(),
            name = this.name,
            disabled = false,
            createdBy = consumer,
            createdDate = LocalDateTime.now()
        )

    private fun CustCatUpdateRequest.toModel(existed: CustomerCategory, consumer: Consumer): CustomerCategory {
        existed.name = this.name
        existed.disabled = this.disabled
        existed.lastModifiedBy = consumer
        existed.lastModifiedDate = LocalDateTime.now()
        return existed
    }

    private fun CustomerCategory.toCustomerResponse(): List<CustomerResponse> =
        this.customer.map {
            CustomerResponse(
                id = it.id,
                name = it.name,
                address = it.address,
                phone = it.phone,
                email = it.email,
                disabled = it.disabled,
                department = it.department,
                categoryId = it.category.id,
                categoryName = it.category.name
            )
        }

}
