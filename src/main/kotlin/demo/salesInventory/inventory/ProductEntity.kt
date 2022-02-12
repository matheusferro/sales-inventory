package demo.salesInventory.inventory

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.FieldType
import org.springframework.data.mongodb.core.mapping.MongoId

@Document
class ProductEntity(
    @Id
    @MongoId(FieldType.STRING)
    val id: String,
    val name: String,
    var quantity: Long,
    var description: String,
    var reservation: Long,
    var status: ProductStatus
) {
    fun needUpdate() = (
            this.quantity == 0L
            || this.reservation == this.quantity
            || (this.reservation < this.quantity && this.status == ProductStatus.OUT_OF_STOCK)
    )

    fun updateFields(updateProductRequest: UpdateProductRequest): ProductEntity = this.apply {
        this.quantity = updateProductRequest.quantity
        this.description = updateProductRequest.description
    }

    fun canDoReservation(reservationQuantity: Long): Boolean = this.reservation.plus(reservationQuantity) < this.quantity

    fun doReservation(reservationProductRequest: ReservationProductRequest): ProductEntity = this.apply {
        assert(reservationProductRequest.quantity < this.quantity)
        this.reservation += reservationProductRequest.quantity
    }

    fun updateStatus(): ProductEntity = this.apply {
            this.status = when {
                reservation == this.quantity -> ProductStatus.OUT_OF_STOCK
                reservation < this.quantity -> ProductStatus.IN_STOCK
                else -> throw Exception("Cannot reserve more than we have")
            }
        }

    fun reservationExpired(reservationExpiredRequest: ReservationExpiredRequest): ProductEntity = this.apply {
        this.reservation = this.reservation.minus(reservationExpiredRequest.quantity)
    }

}