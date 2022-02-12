package demo.salesInventory.inventory

import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SaveProductRequest(
    @field:NotBlank(message = "product name must not be null or blank")
    val name: String,
    @field:NotBlank(message = "product description must not be null or blank")
    val description: String,
    @field:Min(1)
    val quantity: Long,
    @field:NotNull(message = "status must not be null or blank")
    val status: ProductStatus
) {
    fun toEntity(): ProductEntity = ProductEntity(
        id = UUID.randomUUID().toString(),
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        status = this.status,
        reservation = 0L

    )
}

data class SaveProductResponse(
    val id: String
) {
    constructor(entity: ProductEntity) : this(entity.id)
}