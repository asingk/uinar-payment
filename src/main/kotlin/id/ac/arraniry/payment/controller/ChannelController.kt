package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.dto.ChannelCreateRequest
import id.ac.arraniry.payment.dto.ChannelResponse
import id.ac.arraniry.payment.dto.ChannelUpdateRequest
import id.ac.arraniry.payment.dto.CreateUUIDResponse
import id.ac.arraniry.payment.entity.Channel
import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.service.ChannelService
import id.ac.arraniry.payment.service.ConsumerService
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
@RequestMapping("/channels")
class ChannelController(
    private val channelService: ChannelService,
    private val consumerService: ConsumerService,
) {

    @GetMapping
    fun getAll(): List<ChannelResponse> =
        channelService.findAll().map { it.toResponse() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id:UUID): ChannelResponse =
        channelService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "channel does not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody request: ChannelCreateRequest, principal: Principal): CreateUUIDResponse {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        return CreateUUIDResponse(channelService.save(request.toModel(consumer)).id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody request: ChannelUpdateRequest, principal: Principal) {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val exist = channelService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "channel does not found")
        channelService.save(request.toModel(consumer, exist))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) = channelService.delete(id)

    private fun Channel.toResponse(): ChannelResponse =
        ChannelResponse(
            id = this.id,
            name = this.name,
            disabled = this.disabled
        )

    private fun ChannelCreateRequest.toModel(consumer: Consumer): Channel =
        Channel(
            id = UUID.randomUUID(),
            name = this.name,
            disabled = false,
            createdBy = consumer,
            createdDate = LocalDateTime.now()
        )

    private fun ChannelUpdateRequest.toModel(consumer: Consumer, channel: Channel): Channel {
        channel.name = this.name
        channel.disabled = this.disabled
        channel.lastModifiedBy = consumer
        channel.lastModifiedDate = LocalDateTime.now()
        return channel
    }

}