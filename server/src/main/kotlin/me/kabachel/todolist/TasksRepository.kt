package me.kabachel.todolist

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TasksRepository {
    private val tasks = mutableMapOf<Uuid, Task>()

    fun getTasks(): List<Task> {
        return tasks.values.toList()
    }

    fun getTask(uuid: Uuid): Task? {
        return tasks[uuid]
    }

    fun addTask(task: Task): Boolean {
        return tasks.putIfAbsent(task.uuid, task) == null
    }

    fun updateTask(task: Task): Boolean {
        return tasks.replace(task.uuid, task) != null
    }

    fun deleteTask(uuid: Uuid): Boolean {
        return tasks.remove(uuid) != null
    }
}