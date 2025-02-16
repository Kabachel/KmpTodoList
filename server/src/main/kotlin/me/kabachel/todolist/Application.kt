@file:OptIn(ExperimentalUuidApi::class)

package me.kabachel.todolist

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
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
    val tasksRepository = TasksRepository()

    routing {
        route("tasks") {
            get {
                val tasks = tasksRepository.getTasks()
                call.respond(HttpStatusCode.OK, tasks)
            }

            get("{uuid}") {
                val uuid = Uuid.parse(call.parameters["uuid"] ?: throw BadRequestException("Missing uuid"))
                val task = tasksRepository.getTask(uuid) ?: throw NotFoundException("Task not found by uuid: $uuid")
                call.respond(HttpStatusCode.OK, task)
            }

            post {
                val taskRequest = call.receive<Task>()
                val newUuid = Uuid.random()
                val newTask = taskRequest.copy(uuid = newUuid)
                val isSuccess = tasksRepository.addTask(newTask)

                val httpStatusCode = if (isSuccess) HttpStatusCode.Created else HttpStatusCode.InternalServerError
                call.respond(httpStatusCode)
            }

            put {
                val taskRequest = call.receive<Task>()
                val isSuccess = tasksRepository.updateTask(taskRequest)

                val httpStatusCode = if (isSuccess) HttpStatusCode.OK else HttpStatusCode.NotFound
                call.respond(httpStatusCode)
            }

            delete("{uuid}") {
                val uuid = Uuid.parse(call.parameters["uuid"] ?: throw BadRequestException("Missing uuid"))
                val isSuccess = tasksRepository.deleteTask(uuid)

                val httpStatusCode = if (isSuccess) HttpStatusCode.OK else HttpStatusCode.NotFound
                call.respond(httpStatusCode)
            }
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
