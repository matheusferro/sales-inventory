package demo.salesInventory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class SalesInventoryApplication

fun main(args: Array<String>) {
    runApplication<SalesInventoryApplication>(*args)
}
