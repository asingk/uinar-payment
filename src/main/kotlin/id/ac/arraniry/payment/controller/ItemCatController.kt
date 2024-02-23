package id.ac.arraniry.payment.controller

import id.ac.arraniry.payment.dto.CreateUUIDResponse
import id.ac.arraniry.payment.dto.ItemCatRequest
import id.ac.arraniry.payment.dto.ItemCatResponse
import id.ac.arraniry.payment.dto.ItemResponse
import id.ac.arraniry.payment.entity.Consumer
import id.ac.arraniry.payment.entity.ItemCategory
import id.ac.arraniry.payment.service.ConsumerService
import id.ac.arraniry.payment.service.ItemCatService
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
@RequestMapping("item-categories")
class ItemCatController(
    private val itemCatService: ItemCatService,
    private val consumerService: ConsumerService
) {

    @GetMapping
    fun getAll(): List<ItemCatResponse> = itemCatService.findAll().map { it.toResponse() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ItemCatResponse =
        itemCatService.findById(id)?.toResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer category does not found")

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody itemCatRequest: ItemCatRequest, principal: Principal): CreateUUIDResponse {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        return CreateUUIDResponse(itemCatService.save(itemCatRequest.toModel(consumer)).id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody itemCatRequest: ItemCatRequest, principal: Principal) {
        val consumer = consumerService.findByUsernameAndDisabled(principal.name, false)
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "access denied")
        val itemCat = itemCatService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "item category does not found")
        itemCatService.save(itemCatRequest.toModel(consumer, itemCat))
    }

    @GetMapping("/{id}/items")
    fun getItemByCat(@PathVariable id: UUID): List<ItemResponse> =
        itemCatService.findById(id)?.toItemResponse() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "customer category does not found")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID)  = itemCatService.delete(id)

    private fun ItemCategory.toResponse(): ItemCatResponse =
    ItemCatResponse(
        id = this.id,
        name = this.name,
    )

    private fun ItemCatRequest.toModel(consumer: Consumer): ItemCategory =
        ItemCategory(
            id = UUID.randomUUID(),
            name = this.name,
            createdBy = consumer,
            createdDate = LocalDateTime.now()
        )

    private fun ItemCatRequest.toModel(consumer: Consumer, itemCategory: ItemCategory): ItemCategory {
        itemCategory.name = this.name
        itemCategory.lastModifiedBy = consumer
        itemCategory.lastModifiedDate = LocalDateTime.now()
        return itemCategory
    }

    private fun ItemCategory.toItemResponse(): List<ItemResponse> =
        this.item.map {
            ItemResponse(
                id = it.id,
                name = it.name,
                disabled = it.disabled,
                categoryId = it.category.id,
                categoryName = it.category.name
            )
        }

}