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

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    installModules()

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}") {
                headers {
                    append(HttpHeaders.AccessControlAllowOrigin, BASE_URL)
                }
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

private const val BASE_URL = "http://localhost:8080/"