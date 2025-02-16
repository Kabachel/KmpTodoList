package me.kabachel.todolist.tasks

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import me.kabachel.todolist.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TasksRepository {
    private val client = HttpClient(Js) {
        install(Logging)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getTasks(): List<Task> = coroutineScope {
        client.get("$BASE_URL/tasks").body()
    }

    suspend fun getTask(uuid: Uuid): Task = coroutineScope {
        client.get("$BASE_URL/tasks/$uuid").body()
    }

    suspend fun createTask(task: Task): Boolean = coroutineScope {
        client.post("$BASE_URL/tasks") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }.status.isSuccess()
    }

    suspend fun updateTask(task: Task): Boolean = coroutineScope {
        client.put("$BASE_URL/tasks") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }.status.isSuccess()
    }

    suspend fun deleteTask(uuid: Uuid): Boolean = coroutineScope {
        val statusCode = client.delete("$BASE_URL/tasks/$uuid").status
        statusCode.isSuccess()
    }
}

private const val BASE_URL = "http://localhost:8080"