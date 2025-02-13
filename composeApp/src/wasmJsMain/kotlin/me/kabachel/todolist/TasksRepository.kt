package me.kabachel.todolist

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope

class TasksRepository {
    private val client = HttpClient {
        install(Logging)
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getTasks(): List<Task> = coroutineScope {
        client.get("$BASE_URL/tasks").body()
    }
}

private const val BASE_URL = "http://localhost:8080"