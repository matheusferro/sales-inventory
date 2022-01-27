package demo.salesInventory.inventory

import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class SaveProductRequest(
    @field:NotBlank(message = "product name must not be null or blank")
    val name: String,
    @field:NotBlank(message = "product description must not be null or blank")
    val description: String,
    @field:Min(1)
    val quantity: Long
) {
    fun toEntity(): ProductEntity = ProductEntity(
        id = UUID.randomUUID().toString(),
        name = name,
        description = description,
        quantity = quantity
    )
}

data class SaveProductResponse(
    val id: String
) {
    constructor(entity: ProductEntity) : this(entity.id)
}