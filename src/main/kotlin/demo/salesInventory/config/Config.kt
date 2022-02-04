package demo.salesInventory.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer
import java.util.concurrent.Executors

@Configuration
class Config {

    @Bean
    fun messageListenerContainer(
        template: MongoTemplate
    ): MessageListenerContainer {
        println("creating messageListenerContainer")
        val exec = Executors.newSingleThreadExecutor()
        val container = DefaultMessageListenerContainer(template, exec)
        container.start()
        println(container.isRunning)
        return container
    }

}