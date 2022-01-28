package demo.salesInventory

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*

@Document
class MongoDoc (
    @Id
    var id: String,
    var name: String
)

@Repository
interface MongoDocRepository: MongoRepository<MongoDoc, String>

@RestController
@RequestMapping("/mongo")
class ControllerTest(
    private val mongoRepository: MongoDocRepository
) {
    @GetMapping
    fun getAll() = mongoRepository.findAll()

    @PostMapping
    fun save(@RequestBody testDoc: MongoDoc) = mongoRepository.save(testDoc)
}