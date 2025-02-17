package me.kabachel.todolist

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TasksRepository {
    suspend fun getTasks(): List<Task>

    suspend fun getTask(uuid: Uuid): Task?

    suspend fun createTask(task: Task): Boolean

    suspend fun updateTask(task: Task): Boolean

    suspend fun deleteTask(uuid: Uuid): Boolean
}