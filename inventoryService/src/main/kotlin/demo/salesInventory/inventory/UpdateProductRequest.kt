package demo.salesInventory.inventory

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class UpdateProductRequest(
    @field:NotBlank(message = "product description must not be null or blank")
    val description: String,
    @field:Min(1)
    val quantity: Long
) {
    fun updateFields(entityFound: ProductEntity): ProductEntity = entityFound.also {
        it.description = description
        it.quantity = quantity
    }
}

data class UpdateProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val quantity: Long
) {
    constructor(entity: ProductEntity) : this(
        entity.id,
        entity.name,
        entity.description,
        entity.quantity
    )
}