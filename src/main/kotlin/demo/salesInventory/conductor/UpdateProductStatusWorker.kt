package demo.salesInventory.conductor

import com.netflix.conductor.client.worker.Worker
import com.netflix.conductor.common.metadata.tasks.Task
import com.netflix.conductor.common.metadata.tasks.TaskResult
import demo.salesInventory.inventory.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class UpdateProductStatusWorker(
    private val productRepository: ProductRepository
): Worker{
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val prefix = "[${this.javaClass.simpleName}]"

    override fun getTaskDefName(): String = "update_catalog_capacity_status"

    override fun execute(task: Task?): TaskResult {
        logger.info("$prefix[EXECUTE] INITIALIZING TASK ${task?.status} ${task?.taskDefName}")
        val productId = task?.inputData?.get("productId")?.toString() ?: throw IllegalArgumentException("not found productId in task")
        val productFound = productRepository.findById(productId).orElseThrow{ IllegalArgumentException() }
        productRepository.save(productFound.updateStatus())
        val taskResult = TaskResult(task)
            .log("$prefix[EXECUTE] product status updated. productId=$productId, status=${productFound.status.name}, quantity=${productFound.quantity} reservation=${productFound.reservation}")
        return taskResult.apply { this.status = TaskResult.Status.COMPLETED}
    }
}