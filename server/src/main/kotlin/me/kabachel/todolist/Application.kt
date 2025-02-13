package me.kabachel.todolist

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    installModules()

    routing {
        get("tasks") {
            call.respond(HttpStatusCode.OK, tasks)
        }
    }
}

private fun Application.installModules() {
    corsInstall()
    contentNegotiationInstall()
}

private fun Application.corsInstall() {
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        exposeHeader(HttpHeaders.AccessControlAllowOrigin)
    }
}

private fun Application.contentNegotiationInstall() {
    install(ContentNegotiation) {
        json()
    }
}

@OptIn(ExperimentalUuidApi::class)
private val tasks = listOf(
    Task(
        id = Uuid.random(),
        name = "Таска в тудуисте",
        description = "Очень классная таска",
        Task.Priority.Medium
    ),
//    Task(
//        id = Uuid.random(),
//        name = "Write marketing copy",
//        description = "Write a compelling description for the new product",
//        priority = Task.Priority.High,
//    ),
//    Task(
//        id = Uuid.random(),
//        name = "Implement payment flow",
//        description = "Set up a system for processing payments",
//        priority = Task.Priority.Vital,
//    ),
//    Task(
//        id = Uuid.random(),
//        name = "Create user onboarding",
//        description = "Design a seamless onboarding process for new users",
//        priority = Task.Priority.High,
//    ),
//    Task(
//        id = Uuid.random(),
//        name = "Optimize for SEO",
//        description = "Improve search engine rankings to increase organic traffic",
//        priority = Task.Priority.Low,
//    )
)