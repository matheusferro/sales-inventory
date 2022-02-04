package demo.salesInventory.events

import com.mongodb.client.model.changestream.ChangeStreamDocument
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.messaging.*
import org.springframework.stereotype.Component

@Component
class MongoWatcher(
    private val template: MongoTemplate
) : MessageListener<ChangeStreamDocument<Document>, Document> {

    override fun onMessage(message: Message<ChangeStreamDocument<Document>, Document>) {
        val container = DefaultMessageListenerContainer(template)
        container.start()
        val req = ChangeStreamRequest.ChangeStreamRequestOptions(
            "teste",
            "teste",
            ChangeStreamOptions.empty()
        )
        container.register(ChangeStreamRequest(::println, req))
    }
}
