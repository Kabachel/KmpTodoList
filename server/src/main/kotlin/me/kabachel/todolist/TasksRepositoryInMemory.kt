package me.kabachel.todolist

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TasksRepositoryInMemory : TasksRepository {
    private val tasks = mutableMapOf<Uuid, Task>()

    override suspend fun getTasks(): List<Task> {
        return tasks.values.toList()
    }

    override suspend fun getTask(uuid: Uuid): Task? {
        return tasks[uuid]
    }

    override suspend fun createTask(task: Task): Boolean {
        return tasks.putIfAbsent(task.uuid, task) == null
    }

    override suspend fun updateTask(task: Task): Boolean {
        return tasks.replace(task.uuid, task) != null
    }

    override suspend fun deleteTask(uuid: Uuid): Boolean {
        return tasks.remove(uuid) != null
    }
}