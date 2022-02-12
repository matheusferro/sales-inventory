package demo.salesInventory.config

import com.mongodb.client.model.changestream.ChangeStreamDocument
import com.mongodb.client.model.changestream.FullDocument
import demo.salesInventory.conductor.ConductorAPI
import demo.salesInventory.inventory.ProductEntity
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest
import org.springframework.data.mongodb.core.messaging.MessageListener
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer
import org.springframework.data.mongodb.core.messaging.Subscription
import org.springframework.data.mongodb.core.query.Criteria
import java.lang.IllegalArgumentException

@Configuration
class ConductorConfig(
    private val template: MongoTemplate,
    private val messageListenerContainer: MessageListenerContainer,
    private val conductor: ConductorAPI
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun subscriptionBean(): Subscription {
        logger.info("[${this::class.java.simpleName}][subscriptionBean] Subscribing in mongoDB change streams")
        val messageListenerImpl = MessageListener<ChangeStreamDocument<Document>, ProductEntity> { message ->
            logger.info("MessageListener ID:${message.body?.id ?: "ID NULL"}")
            logger.info("operationType: ${message.raw?.operationType?.name}")
            val testBody = object {
                val productId: String = message.body?.id ?: throw IllegalArgumentException("not found id")
                val contentId: String = "contentIdTest"
            }
            conductor.startWorkflow("capacity_status_update", testBody)
        }

        val request = ChangeStreamRequest.builder()
            .collection("productEntity")
            .filter(
                Aggregation.newAggregation(
                    Aggregation.match(
                        Criteria.where("operationType").`in`(
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
        return messageListenerContainer.register(request, ProductEntity::class.java)
    }
}