package demo.salesInventory.conductor

import com.netflix.conductor.client.worker.Worker
import com.netflix.conductor.common.metadata.tasks.Task
import com.netflix.conductor.common.metadata.tasks.TaskResult
import org.springframework.stereotype.Component

@Component
class ProductWorker: Worker {
    override fun getTaskDefName(): String = "verify_if_idents_are_added"

    override fun execute(p0: Task?): TaskResult {
        println("[EXECUTE] INITIALIZING TASK ${p0?.status} ${p0?.taskDefName}")
        val tr = TaskResult(p0)
            .log("worker test")
            .addOutputData("is_idents_added", false)
        tr.status = TaskResult.Status.COMPLETED
        return tr
    }
}