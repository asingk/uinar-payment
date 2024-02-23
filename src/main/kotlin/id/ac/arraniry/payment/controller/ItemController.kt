package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.dto.CreateUUIDResponse
import id.ac.arraniry.payment.dto.ItemCreateRequest
import id.ac.arraniry.payment.dto.ItemResponse
import id.ac.arraniry.payment.dto.ItemUpdateRequest
import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.entity.Item
import id.ac.arraniry.payment.entity.ItemCategory
import id.ac.arraniry.payment.service.ConsumerService
import id.ac.arraniry.payment.service.ItemCatService
import id.ac.arraniry.payment.service.ItemService
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
@RequestMapping("/items")
class ItemController(
    private val itemService: ItemService,
    private val itemCatService: ItemCatService,
    private val consumerService: ConsumerService,
) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ItemResponse =
        itemService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item does not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody itemCreateRequest: ItemCreateRequest, principal: Principal): CreateUUIDResponse {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val cat = itemCatService.findById(itemCreateRequest.categoryId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item category does not found")
        return CreateUUIDResponse(itemService.save(itemCreateRequest.toModel(consumer, cat)).id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody itemUpdateRequest: ItemUpdateRequest, principal: Principal) {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val cat = itemCatService.findById(itemUpdateRequest.categoryId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item category does not found")
        val exist = itemService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item does not found")
        itemService.save(itemUpdateRequest.toModel(consumer, exist, cat))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID) = itemService.delete(id)

    private fun Item.toResponse(): ItemResponse =
        ItemResponse(
            id = this.id,
            name = this.name,
            disabled = this.disabled,
            categoryId = this.category.id,
            categoryName = this.category.name
        )

    private fun ItemCreateRequest.toModel(consumer: Consumer, itemCategory: ItemCategory): Item =
        Item(
            id = UUID.randomUUID(),
            name = this.name,
            disabled = false,
            category = itemCategory,
            createdBy = consumer,
            createdDate = LocalDateTime.now()
        )

    private fun ItemUpdateRequest.toModel(consumer: Consumer, item: Item, itemCategory: ItemCategory): Item {
        item.name = this.name
        item.disabled = this.disabled
        item.category = itemCategory
        item.lastModifiedBy = consumer
        item.lastModifiedDate = LocalDateTime.now()
        return item
    }
}