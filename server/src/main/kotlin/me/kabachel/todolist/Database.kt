@file:OptIn(ExperimentalUuidApi::class)

package me.kabachel.todolist

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

internal fun Application.installDatabase() {
    val dbUrl = "some-url"
    val dbUser = "some-user"
    val dbPassword = "some-password"

    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )

    transaction {
        SchemaUtils.create(TaskTable)
    }
}

object TaskTable : UUIDTable("task") {
    val name = varchar("name", 50)
    val description = varchar("description", 255)
    val priority = varchar("priority", 50)
    val isCompleted = bool("is_completed")
}

class TaskDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TaskDAO>(TaskTable)

    var name by TaskTable.name
    var description by TaskTable.description
    var priority by TaskTable.priority
    var isCompleted by TaskTable.isCompleted
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: TaskDAO) = Task(
    uuid = dao.id.value.toKotlinUuid(),
    name = dao.name,
    description = dao.description,
    priority = Task.Priority.valueOf(dao.priority),
    isCompleted = dao.isCompleted,
)