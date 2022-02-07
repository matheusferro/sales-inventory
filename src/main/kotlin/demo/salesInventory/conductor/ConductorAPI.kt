package demo.salesInventory.conductor

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "NetflixConductorAPI", url = "\${netflix.conductor.url}")
interface ConductorAPI {

    @PostMapping("/api/workflow/{workflowName}")
    fun startWorkflow(
        @PathVariable("workflowName") workflowName: String,
        workflowBody: Any?
    )
}