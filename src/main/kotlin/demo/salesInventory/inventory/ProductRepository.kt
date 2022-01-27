package demo.salesInventory.inventory

import org.springframework.data.mongodb.repository.MongoRepository

interface ProductRepository : MongoRepository<ProductEntity, String>