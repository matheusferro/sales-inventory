package demo.salesInventory

import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import demo.salesInventory.inventory.ProductEntity
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest
import org.springframework.data.mongodb.core.messaging.MessageListener
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class SalesInventoryApplication {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun commandLineRunner(
        template: MongoTemplate,
        messageListenerContainer: MessageListenerContainer
    ): CommandLineRunner = CommandLineRunner {
        //This works with message listener
        val messageListenerImpl = MessageListener<ChangeStreamDocument<Document>, ProductEntity> {
            logger.info("${Thread.currentThread().name} - MessageListener ID:${it.body?.id ?: "ID NULL"}")
            logger.info("operationType: ${it.raw?.operationType?.name}")
        }

        val request = ChangeStreamRequest.builder()
            .collection("productEntity")
            .filter(
                Aggregation.newAggregation(
                    match(
                        where("operationType").`in`(
                            listOf(
                                "insert",
                                "update",
                                "replace"
                            )
                        )
                    )
                )
            )
            .fullDocumentLookup(FullDocument.UPDATE_LOOKUP)
            .publishTo(messageListenerImpl)
            .build()

        messageListenerContainer.register(request, ProductEntity::class.java)
        //Another way to watch change streams
//        val collection = template.getCollection("productEntity")
//        val cursor = collection.watch().iterator()
//
//        cursor.forEachRemaining {
//            logger.info("${it.fullDocument}")
//        }
    }
}

fun main(args: Array<String>) {
    runApplication<SalesInventoryApplication>(*args)
}
