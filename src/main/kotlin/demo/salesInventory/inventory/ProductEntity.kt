package demo.salesInventory.inventory

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.FieldType
import org.springframework.data.mongodb.core.mapping.MongoId

@Document
//@TypeAlias("PRODUCT_ENTITY")
class ProductEntity (
    @Id
    @MongoId(FieldType.STRING)
    val id: String,
    val name: String,
    var quantity: Long,
    var description: String
)