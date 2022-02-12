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
            SaveProductResponse(productRepository.save(saveProductRequest.toEntity()))
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
                        productRepository.save(optProduct.get().updateFields(updateProductRequest))
                    )
                )
            }
        }

    @GetMapping
    fun findAll(): MutableList<ProductEntity> = productRepository.findAll()

    @PatchMapping("/{productId}/reservation")
    fun reservationProduct(
        @PathVariable("productId") productId: String,
        @RequestBody reservationProductRequest: ReservationProductRequest
    ): ResponseEntity<Any> =
        productRepository.findById(productId).let { optProduct ->
            return when (optProduct.isEmpty) {
                true -> ResponseEntity.notFound().build()
                else -> {
                    if(optProduct.get().canDoReservation(reservationProductRequest.quantity)) ResponseEntity.unprocessableEntity()
                    ResponseEntity.ok(
                        UpdateProductResponse(
                            productRepository.save(optProduct.get().doReservation(reservationProductRequest))
                        )
                    )
                }
            }
        }

    @PatchMapping("/{productId}/reservation/expired")
    fun reservationExpired(
        @PathVariable("productId") productId: String,
        @RequestBody reservationExpiredRequest: ReservationExpiredRequest
    ): ResponseEntity<Any> =
        productRepository.findById(productId).let { optProduct ->
            return when (optProduct.isEmpty) {
                true -> ResponseEntity.notFound().build()
                else -> {
                    if(optProduct.get().reservation < reservationExpiredRequest.quantity) ResponseEntity.unprocessableEntity()
                    ResponseEntity.ok(
                        UpdateProductResponse(
                            productRepository.save(optProduct.get().reservationExpired(reservationExpiredRequest))
                        )
                    )
                }
            }
        }
    //todo: function able to abstract not found response
    //private fun executeIfProductExists() {}
}