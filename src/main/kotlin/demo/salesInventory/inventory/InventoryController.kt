package demo.salesInventory.inventory

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/inventory")
class InventoryController(
    private val productRepository: ProductRepository
) {

    @PostMapping
    fun saveProduct(@Valid @RequestBody saveProductRequest: SaveProductRequest): ResponseEntity<SaveProductResponse> =
        ResponseEntity.ok(
            SaveProductResponse(productRepository.save(saveProductRequest.toEntity())).also { println("post") }
        )

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable("productId") productId: String,
        @RequestBody updateProductRequest: UpdateProductRequest
    ): ResponseEntity<Any> =
        productRepository.findById(productId).let { optProduct ->
            return when (optProduct.isEmpty) {
                true -> ResponseEntity.notFound().build()
                else -> ResponseEntity.ok(
                    UpdateProductResponse(
                        productRepository.save(updateProductRequest.updateFields(optProduct.get()))
                    ).also { println("put") }
                )
            }
        }

    @GetMapping
    fun findAll(): MutableList<ProductEntity> = productRepository.findAll()
}