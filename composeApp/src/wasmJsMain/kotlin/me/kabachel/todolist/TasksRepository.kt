package me.kabachel.todolist

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TasksRepository {
    private val tasks = listOf(
        Task(
            id = Uuid.random(),
            name = "Design landing page",
            description = "Create a landing page for the new product",
            Task.Priority.Medium
        ),
        Task(
            id = Uuid.random(),
            name = "Write marketing copy",
            description = "Write a compelling description for the new product",
            priority = Task.Priority.High,
        ),
        Task(
            id = Uuid.random(),
            name = "Implement payment flow",
            description = "Set up a system for processing payments",
            priority = Task.Priority.Vital,
        ),
        Task(
            id = Uuid.random(),
            name = "Create user onboarding",
            description = "Design a seamless onboarding process for new users",
            priority = Task.Priority.High,
        ),
        Task(
            id = Uuid.random(),
            name = "Optimize for SEO",
            description = "Improve search engine rankings to increase organic traffic",
            priority = Task.Priority.Low,
        )
    )

    private val client = HttpClient {
        install(Logging)
    }

    fun getTasks(): List<Task> {
        return tasks
    }
}