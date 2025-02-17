package me.kabachel.todolist

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@OptIn(ExperimentalUuidApi::class)
internal class TasksRepositoryDatabase : TasksRepository {
    override suspend fun getTasks(): List<Task> = suspendTransaction {
        TaskDAO.all().map(::daoToModel)
    }

    override suspend fun getTask(uuid: Uuid): Task? = suspendTransaction {
        TaskDAO.find { (TaskTable.id eq uuid.toJavaUuid()) }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun createTask(task: Task): Boolean = suspendTransaction {
        TaskDAO.new {
            name = task.name
            description = task.description
            priority = task.priority.value
            isCompleted = task.isCompleted
        }
        true
    }

    override suspend fun updateTask(task: Task): Boolean = suspendTransaction {
        TaskDAO.findByIdAndUpdate(task.uuid.toJavaUuid()) {
            it.name = task.name
            it.description = task.description
            it.priority = task.priority.value
            it.isCompleted = task.isCompleted
        }
        true
    }

    override suspend fun deleteTask(uuid: Uuid): Boolean = suspendTransaction {
        val rowsDeleted = TaskTable.deleteWhere {
            TaskTable.id eq uuid.toJavaUuid()
        }
        rowsDeleted == 1
    }
}