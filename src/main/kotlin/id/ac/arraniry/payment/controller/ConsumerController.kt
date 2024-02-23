package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.dto.CreateUUIDResponse
import id.ac.arraniry.payment.dto.ConsumerCreateRequest
import id.ac.arraniry.payment.dto.ConsumerResponse
import id.ac.arraniry.payment.dto.ConsumerUpdateRequest
import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.service.ConsumerService
import id.ac.arraniry.payment.service.RoleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
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
@RequestMapping("/consumers")
class ConsumerController(
    private val consumerService: ConsumerService,
    private val passwordEncoder: PasswordEncoder,
    private val roleService: RoleService,
) {

    @GetMapping
    fun getAll(): List<ConsumerResponse> = consumerService.findAll().map { it.toResponse() }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody consumerRequest: ConsumerCreateRequest, principal: Principal): CreateUUIDResponse {
        val createdBy = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        return CreateUUIDResponse(consumerService.save(consumerRequest.toModel(createdBy.id)).id)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ConsumerResponse =
        consumerService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "consumer does not found")

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody consumerRequest: ConsumerUpdateRequest, principal: Principal) {
        val updatedBy = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val existed = consumerService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "consumer does not found")
        consumerService.save(consumerRequest.toModel(existed, updatedBy.id))
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) =
        consumerService.delete(id)

    private fun Consumer.toResponse(): ConsumerResponse =
        ConsumerResponse(
            id = this.id,
            username = this.username,
            disabled = this.disabled,
            role = this.role.code,
        )

    private fun ConsumerCreateRequest.toModel(createdBy: UUID): Consumer {
        val role = roleService.findById(this.role) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "role does not found")
        return Consumer(
            id = UUID.randomUUID(),
            username = this.username,
            password = passwordEncoder.encode(this.password),
            role = role,
            createdBy = createdBy,
            createdDate = LocalDateTime.now()
        )
    }

    private fun ConsumerUpdateRequest.toModel(consumer: Consumer, updatedBy: UUID): Consumer {
        consumer.username = this.username
        consumer.password = passwordEncoder.encode(this.password)
        consumer.disabled = this.disabled
        val role = roleService.findById(this.role) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "role does not found")
        consumer.role = role
        consumer.lastModifiedBy = updatedBy
        consumer.lastModifiedDate = LocalDateTime.now()
        return consumer
    }
}