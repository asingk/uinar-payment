package id.ac.arraniry.payment.repo

import id.ac.arraniry.payment.entity.ItemCategory
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ItemCatRepo: CrudRepository<ItemCategory, UUID>