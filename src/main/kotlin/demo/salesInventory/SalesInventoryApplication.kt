package demo.salesInventory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories
class SalesInventoryApplication

fun main(args: Array<String>) {
    runApplication<SalesInventoryApplication>(*args)
}
