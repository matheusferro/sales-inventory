package demo.salesInventory.conductor

import com.netflix.conductor.client.worker.Worker
import com.netflix.conductor.common.metadata.tasks.Task
import com.netflix.conductor.common.metadata.tasks.TaskResult
import demo.salesInventory.inventory.ProductRepository
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class CalculateCapacityWorker(
    private val productRepository: ProductRepository
): Worker {
    override fun getTaskDefName(): String = "calculate_capacity_status"

    override fun execute(task: Task?): TaskResult {
        println("[EXECUTE] INITIALIZING TASK ${task?.status} ${task?.taskDefName}")
        val productId = task?.inputData?.get("productId")?.toString() ?: throw IllegalArgumentException("not found productId in task")
        val productFound = productRepository.findById(productId).orElseThrow{IllegalArgumentException()}
        val tr = TaskResult(task)
            .log("worker test")
            .addOutputData("converter_decision", productFound.needUpdate())
        tr.status = TaskResult.Status.COMPLETED
        return tr
    }
}