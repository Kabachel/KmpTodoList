package me.kabachel.todolist

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        exposeHeader(HttpHeaders.AccessControlAllowOrigin)
    }
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

private const val BASE_URL = "http://localhost:8080/"