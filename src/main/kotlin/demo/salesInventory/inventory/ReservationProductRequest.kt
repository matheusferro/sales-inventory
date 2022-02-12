package demo.salesInventory.inventory

import javax.validation.constraints.Min

data class ReservationProductRequest(
    @field:Min(1)
    val quantity: Long
){
    constructor(): this(1)
}

data class ReservationExpiredRequest(
    @field:Min(1)
    val quantity: Long
){
    constructor(): this(1)
}