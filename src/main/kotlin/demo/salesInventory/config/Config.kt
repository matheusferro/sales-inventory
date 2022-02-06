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
    fun messageListenerContainerConfig(
        template: MongoTemplate
    ): MessageListenerContainer {
        /**
         *  Here we can use the simple configuration:
         *  val container = DefaultMessageListenerContainer(template)
         *  container.start()
         *  return container
         */

        // Or change the thread which will run messageListener
        val container = DefaultMessageListenerContainer(
            template,
            Executors.newSingleThreadExecutor()
        )
        container.start()
        return container
    }

}